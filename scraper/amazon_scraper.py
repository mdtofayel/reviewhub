"""
amazon_scraper.py

Reusable Amazon scraper for ReviewHub scraping agent.
Takes a search term and returns structured product data.
"""

import time
import json
import requests
from bs4 import BeautifulSoup
from typing import List, Dict, Any, Optional

# Basic headers to pretend to be a normal browser
DEFAULT_HEADERS = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64 "
        "AppleWebKit/537.36 (KHTML, like Gecko) "
        "Chrome/122.0.0.0 Safari/537.36"
    ),
    "Accept-Language": "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7",
}

AMAZON_BASE_URL = "https://www.amazon.de"


def build_search_url(query: str, page: int = 1) -> str:
    """Build the Amazon search url for a given query and page."""
    from urllib.parse import quote_plus

    encoded = quote_plus(query)
    return f"{AMAZON_BASE_URL}/s?k={encoded}&page={page}"


def extract_product_links(search_html: str, base_url: str = AMAZON_BASE_URL) -> List[str]:
    """Extract unique product detail page links from a search result html."""
    soup = BeautifulSoup(search_html, "html.parser")
    product_links: List[str] = []

    for link_tag in soup.find_all("a", href=True):
        href = link_tag["href"]
        if "/dp/" in href:
            full_link = base_url + href.split("?")[0]
            if full_link not in product_links:
                product_links.append(full_link)

    return product_links


def parse_product_page(html: str, url: str) -> Dict[str, Any]:
    """Parse a single product page and return structured data."""
    soup = BeautifulSoup(html, "html.parser")

    # Title
    title_tag = soup.select_one("#productTitle, span#title")
    title = title_tag.get_text(strip=True) if title_tag else "N/A"

    # Price
    price_tag = soup.select_one("span.a-price span.a-offscreen")
    price = price_tag.get_text(strip=True) if price_tag else "N/A"

    # Rating text like "4,5 von 5 Sternen"
    rating_tag = soup.select_one("span.a-icon-alt")
    rating_text = rating_tag.get_text(strip=True) if rating_tag else "N/A"

    # Review count
    reviews_tag = soup.select_one("#acrCustomerReviewText")
    reviews_text = reviews_tag.get_text(strip=True) if reviews_tag else "N/A"

    # Availability
    availability_tag = soup.select_one("#availability span, span.a-size-medium.a-color-success")
    availability = availability_tag.get_text(strip=True) if availability_tag else "N/A"

    # Bullet points
    bullets = soup.select("ul.a-unordered-list.a-vertical span.a-list-item")
    bullet_texts = [b.get_text(strip=True) for b in bullets if b.get_text(strip=True)]
    description_bullets = " | ".join(bullet_texts) if bullet_texts else "N/A"

    # Long description with fallback to bullets
    long_desc_tag = soup.select_one("#productDescription")
    if long_desc_tag:
        long_description = long_desc_tag.get_text(strip=True)
    else:
        long_description = description_bullets

    # Main image url
    img_tag = soup.select_one("#imgTagWrapperId img, img#landingImage")
    image_url = None
    if img_tag:
        image_url = img_tag.get("data-old-hires") or img_tag.get("src")

    # Technical details table
    tech_details: Dict[str, str] = {}
    table = soup.find("table", id="productDetails_techSpec_section_1")
    if table:
        for row in table.select("tr"):
            key_cell = row.find("th")
            val_cell = row.find("td")
            if key_cell and val_cell:
                key = key_cell.get_text(strip=True)
                val = val_cell.get_text(strip=True)
                tech_details[key] = val

    # Simple ASIN extraction from url
    asin = None
    try:
        parts = url.split("/dp/")
        if len(parts) > 1:
            asin = parts[1].split("/")[0].split("?")[0]
    except Exception:
        asin = None

    return {
        "url": url,
        "asin": asin,
        "title": title,
        "price": price,
        "rating_text": rating_text,
        "reviews_text": reviews_text,
        "availability": availability,
        "description_bullets": description_bullets,
        "long_description": long_description,
        "technical_details": tech_details,
        "image_url": image_url,
    }


def fetch_html(url: str, headers: Optional[Dict[str, str]] = None, timeout: int = 15) -> str:
    """Fetch html from url with basic error handling."""
    h = headers or DEFAULT_HEADERS
    res = requests.get(url, headers=h, timeout=timeout)
    res.raise_for_status()
    return res.text


def scrape_amazon_products_for_query(
    query: str,
    max_products: int = 20,
    sleep_seconds: int = 5,
    headers: Optional[Dict[str, str]] = None,
) -> List[Dict[str, Any]]:
    """
    Main entry for our project.

    Input
      query        search term, for example "drill" or "gaming laptop"
      max_products maximum number of products to scrape
      sleep_seconds pause between product requests
    """
    headers = headers or DEFAULT_HEADERS

    collected_products: List[Dict[str, Any]] = []
    seen_links: set[str] = set()

    page = 1
    while len(collected_products) < max_products:
        search_url = build_search_url(query, page=page)
        print(f"Fetching search result page {page}: {search_url}")

        try:
            search_html = fetch_html(search_url, headers=headers)
        except Exception as e:
            print(f"Failed to fetch search page {page}: {e}")
            break

        links = extract_product_links(search_html, base_url=AMAZON_BASE_URL)
        if not links:
            print("No more product links found. Stop.")
            break

        # Only new links
        new_links = [l for l in links if l not in seen_links]
        if not new_links:
            print("No new product links on this page. Stop.")
            break

        for url in new_links:
            if len(collected_products) >= max_products:
                break

            print(f"Product {len(collected_products)+1}/{max_products}: {url}")
            try:
                html = fetch_html(url, headers=headers)
                details = parse_product_page(html, url)
                collected_products.append(details)
            except Exception as e:
                print(f"Error while scraping product {url}: {e}")
                collected_products.append(
                    {
                        "url": url,
                        "asin": None,
                        "title": "N/A",
                        "price": "N/A",
                        "rating_text": "N/A",
                        "reviews_text": "N/A",
                        "availability": "N/A",
                        "description_bullets": "N/A",
                        "long_description": "N/A",
                        "technical_details": {},
                        "error": str(e),
                    }
                )

            seen_links.add(url)
            time.sleep(sleep_seconds)

        page += 1

    return collected_products


if __name__ == "__main__":
    # Example local test run
    query = "drill"
    products = scrape_amazon_products_for_query(query=query, max_products=10)

    # For local debug we can write a json file
    out_file = "amazon_products_drill.json"
    with open(out_file, "w", encoding="utf8") as f:
        json.dump(products, f, ensure_ascii=False, indent=2)

    print(f"Saved {len(products)} products to {out_file}")
