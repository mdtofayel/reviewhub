import argparse
import json
import time

import requests
from bs4 import BeautifulSoup


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("job_id")
    parser.add_argument("backend_url")
    parser.add_argument("search_url")
    parser.add_argument("limit", type=int)
    return parser.parse_args()


HEADERS = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) "
        "Chrome/122.0.0.0 Safari/537.36"
    )
}


def scrape_product(url: str) -> dict:
    res = requests.get(url, headers=HEADERS, timeout=20)
    res.raise_for_status()

    soup = BeautifulSoup(res.text, "html.parser")

    title_tag = soup.select_one("#productTitle, span#title")
    title = title_tag.get_text(strip=True) if title_tag else "N/A"

    price_tag = soup.select_one("span.a-price span.a-offscreen")
    price = price_tag.get_text(strip=True) if price_tag else "N/A"

    rating_tag = soup.select_one("span.a-icon-alt")
    rating = rating_tag.get_text(strip=True) if rating_tag else "N/A"

    reviews_tag = soup.select_one("#acrCustomerReviewText")
    reviews = reviews_tag.get_text(strip=True) if reviews_tag else "N/A"

    availability_tag = soup.select_one(
        "#availability span, span.a-size-medium.a-color-success"
    )
    availability = availability_tag.get_text(strip=True) if availability_tag else "N/A"

    bullets = soup.select("ul.a-unordered-list.a-vertical span.a-list-item")
    description_bullets = (
        " | ".join(b.get_text(strip=True) for b in bullets if b.get_text(strip=True))
        if bullets
        else "N/A"
    )

    long_desc_tag = soup.select_one("#productDescription")
    long_description = (
        long_desc_tag.get_text(strip=True) if long_desc_tag else "N/A"
    )

    tech_details = {}
    table = soup.find("table", id="productDetails_techSpec_section_1")
    if table:
        for row in table.select("tr"):
            key_cell = row.find("th")
            val_cell = row.find("td")
            if key_cell and val_cell:
                key = key_cell.get_text(strip=True)
                val = val_cell.get_text(strip=True)
                tech_details[key] = val

    image_url = None  # later we will add real selector

    return {
        "url": url,
        "title": title,
        "price": price,
        "rating": rating,
        "reviews": reviews,
        "availability": availability,
        "description_bullets": description_bullets,
        "long_description": long_description,
        "technical_details_json": json.dumps(tech_details),
        "category_guess": None,
        "image_url": image_url,
    }


def send_raw_product(backend_url: str, job_id: str, data: dict) -> None:
    payload = {
        "url": data["url"],
        "title": data["title"],
        "price": data["price"],
        "rating": data["rating"],
        "reviews": data["reviews"],
        "availability": data["availability"],
        "descriptionBullets": data["description_bullets"],
        "longDescription": data["long_description"],
        "technicalDetailsJson": data["technical_details_json"],
        "categoryGuess": data["category_guess"],
        "imageUrl": data["image_url"],
    }

    resp = requests.post(
        f"{backend_url.rstrip('/')}/api/scrape-jobs/{job_id}/raw-products",
        json=payload,
        timeout=20,
    )
    print("Sent raw row:", data["url"], "status:", resp.status_code)


def main():
    args = parse_args()
    job_id = args.job_id
    backend_url = args.backend_url
    search_url = args.search_url
    limit = args.limit

    print("Starting search scraper for job", job_id)
    print("Search url:", search_url)
    print("Limit:", limit)

    response = requests.get(search_url, headers=HEADERS, timeout=20)
    if response.status_code != 200:
        print("Failed to fetch search page:", response.status_code)
        return 1

    soup = BeautifulSoup(response.text, "html.parser")

    product_links = []
    for link_tag in soup.find_all("a", href=True):
        href = link_tag["href"]
        if "/dp/" in href:
            full_link = "https://www.amazon.de" + href.split("?")[0]
            if full_link not in product_links:
                product_links.append(full_link)
        if len(product_links) >= limit:
            break

    print("Collected", len(product_links), "product links")

    for index, url in enumerate(product_links, start=1):
        print(f"[{index}/{len(product_links)}] Scraping", url)
        product = scrape_product(url)
        send_raw_product(backend_url, job_id, product)
        time.sleep(5)

    print("Search scraper finished for job", job_id)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
