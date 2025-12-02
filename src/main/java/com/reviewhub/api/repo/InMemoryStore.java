// src/main/java/com/reviewhub/api/repo/InMemoryStore.java
package com.reviewhub.api.repo;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.reviewhub.api.model.Faq;
import com.reviewhub.api.model.JobLog;
import com.reviewhub.api.model.Product;
import com.reviewhub.api.model.Review;
import com.reviewhub.api.model.RoundupArticle;
import com.reviewhub.api.model.ScrapeJob;
import com.reviewhub.api.model.Seo;
import com.reviewhub.api.model.DealArticle;
import com.reviewhub.api.model.DealProduct;

import jakarta.annotation.PostConstruct;

import com.reviewhub.api.model.TestMetricRow;
import com.reviewhub.api.model.SpecRow;


@Component
public class InMemoryStore {

  public final Map<String, DealArticle> dealsBySlug = new ConcurrentHashMap<>();

  public final Map<String, Product> productsBySlug = new ConcurrentHashMap<>();
  public final Set<String> categories = new HashSet<>();
  public final Map<String, RoundupArticle> roundupsBySlug = new ConcurrentHashMap<>();

  public final Map<String, ScrapeJob> jobsById = new ConcurrentHashMap<>();
  public final Map<String, List<JobLog>> logsByJobId = new ConcurrentHashMap<>();
  
  

  @PostConstruct
  public void seed() {
    // simple sample categories and products
    categories.addAll(List.of("laptops", "headphones", "monitors", "smartphones"));

    for (int i = 1; i <= 30; i++) {
      String slug = "product-" + i;
      Product p = new Product(
          UUID.randomUUID().toString(),
          slug,
          "Mta product " + i,
          (i % 2 == 0 ? "Acme" : "Contoso"),
          (i % 3 == 0 ? null : 99.99 + i),
          "EUR",
          "https://picsum.photos/seed/" + slug + "/800/600",
          3.5 + (i % 15) * 0.1,
          20 + i,
          List.of((i % 2 == 0) ? "Editors choice" : "Great value"),
          Map.of(
              "Weight", "1." + i + " kg",
              "Color", (i % 2 == 0 ? "Black" : "Silver")
          ),
          new Review(
              "Quick verdict for product " + i,
              new String[]{"Solid build", "Good battery"},
              new String[]{"Average speakers"},
              "## Full Markdown Review\n\nThis is a demo body for product " + i + ".",
              "Reviewer " + ((i % 5) + 1),
              Instant.now().toString()
          ),
          new Seo("Sample product " + i, "SEO description for product " + i),
          Instant.now(),
          i % 2 == 0 ? "laptops" : "headphones"
      );
      productsBySlug.put(slug, p);
    }
    
    Instant now = Instant.now();

    var dealProducts = List.of(
        new DealProduct(
            "dp1",
            "Amazon Fire TV Stick 4K",
            "https://picsum.photos/seed/deal1/260/260",
            "amazon.de",
            "CHECK PRICE",
            "https://amazon.de/firestick"
        ),
        new DealProduct(
            "dp2",
            "Amazon Kindle Paperwhite 16GB",
            "https://picsum.photos/seed/deal2/260/260",
            "amazon.de",
            "CHECK PRICE",
            "https://amazon.de/kindle"
        ),
        new DealProduct(
            "dp3",
            "Oura Ring Gen4 Smart Ring",
            "https://picsum.photos/seed/deal3/260/260",
            "amazon.de",
            "CHECK PRICE",
            "https://amazon.de/oura"
        )
    );

    DealArticle xboxDeal = new DealArticle(
        "black-friday-xbox-elite-series-2",
        "We have found a huge Black Friday saving on the Xbox Elite Series 2",
        "Deals",
        "https://picsum.photos/seed/xbox-elite-hero/1200/600",
        "Gian Estrada",
        now,
        """
        A highly customisable Xbox controller with swappable parts and long battery life
        is now available in the Black Friday sale for much less.
        """,
        """
        The Xbox Elite Wireless Controller Series 2 has dropped in price for a limited time.

        It is perfect if you want something more advanced than the standard controller for
        competitive multiplayer or long single player sessions.

        Battery life is one of its strong points and the build quality is made for heavy play.
        """,
        new Seo(
            "Black Friday deal on Xbox Elite Series 2",
            "Big discount on the Xbox Elite Series 2 controller during the Black Friday sale."
        ),
        "Best Early Black Friday Deals 2025",
        dealProducts
    );

    dealsBySlug.put(xboxDeal.slug(), xboxDeal);

    	

    // one roundup article for best mid range smartphones
  
    List<RoundupArticle.RoundupProduct> phones = List.of(
        new RoundupArticle.RoundupProduct(
            "phone-1",
            "google-pixel-9a",
            1,
            "Google Pixel 9a",
            "Google",
            549.0,
            "EUR",
            "https://picsum.photos/seed/pixel9a/800/600",
            4.7,
            210,
            "The best mid range phone for most people.",
            "Great camera and long term software support at a reasonable price.",
            List.of(
                "Very strong camera",
                "Clean Android software",
                "Long update promise"
            ),
            List.of(
                "Charging could be faster",
                "No true telephoto camera"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/pixel9a",
                "O2", "https://example.com/o2/pixel9a",
                "MediaMarkt", "https://example.com/mediamarkt/pixel9a"
            ),
            "product-1"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-2",
            "samsung-galaxy-a55",
            2,
            "Samsung Galaxy A55",
            "Samsung",
            499.0,
            "EUR",
            "https://picsum.photos/seed/a55/800/600",
            4.5,
            180,
            "Premium feeling mid range phone with great screen.",
            "A refined option with strong display and build quality.",
            List.of(
                "Bright oled screen",
                "Premium build for this price",
                "Solid battery life"
            ),
            List.of(
                "Camera in low light is only average",
                "Charging slower than rivals"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/a55",
                "Simde", "https://example.com/simde/a55",
                "MediaMarkt", "https://example.com/mediamarkt/a55"
            ),
            "product-2"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-3",
            "poco-f7-ultra",
            3,
            "Poco F7 Ultra",
            "Poco",
            399.0,
            "EUR",
            "https://picsum.photos/seed/pocof7/800/600",
            4.3,
            150,
            "Performance king at a friendly price.",
            "Perfect for users who want speed above everything else.",
            List.of(
                "Very fast chip",
                "High refresh rate display",
                "Good value for money"
            ),
            List.of(
                "Software has some clutter",
                "Cameras behind the very best in class"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/pocof7",
                "O2", "https://example.com/o2/pocof7",
                "MediaMarkt", "https://example.com/mediamarkt/pocof7"
            ),
            "product-3"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-4",
            "oneplus-nord-4",
            4,
            "OnePlus Nord 4",
            "OnePlus",
            429.0,
            "EUR",
            "https://picsum.photos/seed/nord4/800/600",
            4.2,
            130,
            "Smooth performance with OxygenOS polish.",
            "Great everyday phone with clean software and fast feel.",
            List.of(
                "Very smooth user experience",
                "Fast charging",
                "Nice design"
            ),
            List.of(
                "Camera could be better at night",
                "No wireless charging"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/nord4",
                "Simde", "https://example.com/simde/nord4",
                "MediaMarkt", "https://example.com/mediamarkt/nord4"
            ),
            "product-4"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-5",
            "xiaomi-14t-pro",
            5,
            "Xiaomi 14T Pro",
            "Xiaomi",
            599.0,
            "EUR",
            "https://picsum.photos/seed/xiaomi14t/800/600",
            4.4,
            160,
            "Flagship style performance at a mid level price.",
            "Great choice if you want power and fast charging.",
            List.of(
                "Very fast chip",
                "Bright display",
                "Rapid charging"
            ),
            List.of(
                "Software will not suit everyone",
                "Battery life only average"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/xiaomi14t",
                "O2", "https://example.com/o2/xiaomi14t",
                "MediaMarkt", "https://example.com/mediamarkt/xiaomi14t"
            ),
            "product-5"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-6",
            "motorola-edge-50",
            6,
            "Motorola Edge 50",
            "Motorola",
            429.0,
            "EUR",
            "https://picsum.photos/seed/edge50/800/600",
            4.0,
            110,
            "Perfect for fans of clean Android.",
            "Nice design and close to stock software.",
            List.of(
                "Close to stock Android",
                "Comfortable design",
                "Good everyday performance"
            ),
            List.of(
                "Camera is decent but not class leading",
                "Update policy not as strong as Google or Samsung"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/edge50",
                "Simde", "https://example.com/simde/edge50",
                "MediaMarkt", "https://example.com/mediamarkt/edge50"
            ),
            "product-6"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-7",
            "sony-xperia-10v",
            7,
            "Sony Xperia 10V",
            "Sony",
            379.0,
            "EUR",
            "https://picsum.photos/seed/xperia10v/800/600",
            3.9,
            90,
            "Niche choice that is great for media.",
            "Tall screen and good audio for content lovers.",
            List.of(
                "Nice tall oled display",
                "Good stereo speakers",
                "Very light body"
            ),
            List.of(
                "Chipset is not very fast",
                "Camera results vary"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/xperia10v",
                "O2", "https://example.com/o2/xperia10v",
                "MediaMarkt", "https://example.com/mediamarkt/xperia10v"
            ),
            "product-7"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-8",
            "realme-gt3-lite",
            8,
            "Realme GT3 Lite",
            "Realme",
            349.0,
            "EUR",
            "https://picsum.photos/seed/gt3lite/800/600",
            4.1,
            100,
            "Best choice for users who value speed.",
            "Strong performance and fast charging for the price.",
            List.of(
                "Very fast charging",
                "Good performance",
                "Nice flat display"
            ),
            List.of(
                "Software has some extra apps",
                "Camera does not match the very best here"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/gt3lite",
                "Simde", "https://example.com/simde/gt3lite",
                "MediaMarkt", "https://example.com/mediamarkt/gt3lite"
            ),
            "product-8"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-9",
            "honor-400-pro",
            9,
            "Honor 400 Pro",
            "Honor",
            569.0,
            "EUR",
            "https://picsum.photos/seed/honor400pro/800/600",
            4.3,
            140,
            "Excellent option for entertainment lovers.",
            "Large bright display and strong speakers.",
            List.of(
                "Large bright screen",
                "Good speakers",
                "Fast charging"
            ),
            List.of(
                "Software will not be to everyones taste",
                "Availability can vary by region"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/honor400pro",
                "O2", "https://example.com/o2/honor400pro",
                "MediaMarkt", "https://example.com/mediamarkt/honor400pro"
            ),
            "product-9"
        ),
        new RoundupArticle.RoundupProduct(
            "phone-10",
            "nothing-phone-3a",
            10,
            "Nothing Phone 3A",
            "Nothing",
            449.0,
            "EUR",
            "https://picsum.photos/seed/nothing3a/800/600",
            4.2,
            125,
            "Stylish pick with solid performance.",
            "Great design and clean software in this price band.",
            List.of(
                "Very distinctive design",
                "Clean software",
                "Good overall performance"
            ),
            List.of(
                "Camera only average in low light",
                "No telephoto camera"
            ),
            Map.of(
                "Amazon", "https://example.com/amazon/nothing3a",
                "Simde", "https://example.com/simde/nothing3a",
                "MediaMarkt", "https://example.com/mediamarkt/nothing3a"
            ),
            "product-10"
        )
    );

 // ========= CREATE MULTIPLE OPINION / ROUNDUP ARTICLES =========

 // Use your original object as a template and duplicate with variations
 List<RoundupArticle> opinionArticles = List.of(
     new RoundupArticle(
         "best-mid-range-smartphones-2025",
         "Best mid range smartphones 2025",
         "Great value handsets with flagship style features.",
         "https://picsum.photos/seed/midrange-hero/1200/600",
         "ReviewHub team",
         now.minusSeconds(7 * 24 * 3600),
         now,
         """
         In the world of smartphones, mid range handsets offer a strong mix of value and features.
         Our guide collects the stand out models and explains which one suits different kinds of buyers.
         """,
         phones,
         """
         ## How to choose a mid range phone
         Look for at least six gigabytes of memory, a fast processor, a bright display and a large battery.
         """,
         """
         ## How we test mid range phones
         We live with each phone for several days.
         """,
         List.of(
             new Faq("Is a mid range phone enough for gaming?", "Yes for many games."),
             new Faq("How long should a mid range phone last?", "Two to three years.")
         ),
         "If you want strong performance without flagship prices, these are solid options.",
         new Seo("Best mid range smartphones 2025",
                 "Guide to the best mid price phones with buying advice."),
         "phones",
         testData,
         fullSpecs
     ),

     // ========= DUPLICATE 2 =========
     new RoundupArticle(
         "tech-gift-ideas-2025",
         "17 of the very best tech gifts to buy this Christmas",
         "Perfect tech gifts for everyone.",
         "https://picsum.photos/seed/gifts/1200/600",
         "Max Parker",
         now.minusSeconds(3 * 24 * 3600),
         now,
         "A fun and practical selection of tech gift ideas.",
         phones,
         "Gift guide intro",
         "Gift testing info",
         List.of(),
         "These products make great gifts.",
         new Seo("Tech gifts 2025", "Christmas tech buying guide."),
         "gadgets",
         testData,
         fullSpecs
     ),

     // ========= DUPLICATE 3 =========
     new RoundupArticle(
         "jbl-soundbar-opinion",
         "JBL’s soundbars offer bang for buck, but there’s one area they could do better",
         "Opinion on JBL soundbars.",
         "https://picsum.photos/seed/jbl/1200/600",
         "Kob Monney",
         now.minusSeconds(2 * 24 * 3600),
         now,
         "Why JBL soundbars impress but still have flaws.",
         phones,
         "Opinion intro",
         "Opinion testing",
         List.of(),
         "JBL is close to perfection, but needs improvement.",
         new Seo("JBL Soundbars Opinion", "Opinion on the JBL 2025 soundbar range."),
         "audio",
         testData,
         fullSpecs
     ),

     // ========= DUPLICATE 4 =========
     new RoundupArticle(
         "winter-window-opinion",
         "Just because it's cold outside, it doesn't mean you shouldn't open your windows",
         "A household opinion piece.",
         "https://picsum.photos/seed/windows/1200/600",
         "David Ludlow",
         now.minusSeconds(24 * 3600),
         now,
         "Fresh air still matters in winter.",
         phones,
         "Ventilation intro",
         "Ventilation testing",
         List.of(),
         "Open windows for healthier living.",
         new Seo("Winter ventilation opinion", "Why fresh air matters in winter."),
         "lifestyle",
         testData,
         fullSpecs
     ),

     // ========= DUPLICATE 5 =========
     new RoundupArticle(
         "google-iphone-fix",
         "I can't believe Google has solved this long-standing iPhone issue",
         "Tech opinion piece.",
         "https://picsum.photos/seed/googlefix/1200/600",
         "Lewis Painter",
         now.minusSeconds(24 * 3600),
         now,
         "Google finally solved a problem iPhone users suffered for years.",
         phones,
         "Opinion intro",
         "Opinion test",
         List.of(),
         "The fix is surprisingly elegant.",
         new Seo("Google iPhone Fix", "Google solves major iPhone flaw."),
         "smartphones",
         testData,
         fullSpecs
     ),

     // ========= DUPLICATE 6 =========
     new RoundupArticle(
         "future-tech-predictions",
         "Five tech predictions that will shape 2026",
         "What the next year of innovation will bring.",
         "https://picsum.photos/seed/futuretech/1200/600",
         "ReviewHub team",
         now.minusSeconds(5 * 24 * 3600),
         now,
         "AI, AR and silicon changes are coming.",
         phones,
         "Predictions intro",
         "Prediction method",
         List.of(),
         "Expect rapid innovation in 2026.",
         new Seo("Tech predictions 2026", "Upcoming trends in consumer technology."),
         "analysis",
         testData,
         fullSpecs
     )
 );

 // add all into map
 for (RoundupArticle article : opinionArticles) {
     roundupsBySlug.put(article.slug(), article);
 }

  }
  List<TestMetricRow> testData = List.of(
		    new TestMetricRow(
		        "1 hour music streaming (offline)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "1 %"
		        )
		    ),
		    new TestMetricRow(
		        "1 hour music streaming (online)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "1 %"
		        )
		    ),
		    new TestMetricRow(
		        "1 hour video playback (Netflix, HDR)",
		        Map.of(
		            "Xiaomi 14T Pro", "6 %",
		            "OnePlus 13R", "6 %",
		            "Google Pixel 9a", "1 %",
		            "Nothing Phone 3a Pro", "5 %",
		            "Poco F7 Ultra", "6 %",
		            "Honor 400 Pro", "4 %",
		            "Apple iPhone 16e", "4 %"
		        )
		    ),
		    new TestMetricRow(
		        "15 min recharge (included charger)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "25 %",
		            "Poco F7 Ultra", "58 %",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "–"
		        )
		    ),
		    new TestMetricRow(
		        "15 min recharge (no charger included)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "18 %",
		            "Google Pixel 9a", "22 %",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "44 %",
		            "Apple iPhone 16e", "29 %"
		        )
		    ),
		    new TestMetricRow(
		        "30 minute gaming (intensive)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "4 %"
		        )
		    ),
		    new TestMetricRow(
		        "30 minute gaming (light)",
		        Map.of(
		            "Xiaomi 14T Pro", "6 %",
		            "OnePlus 13R", "3 %",
		            "Google Pixel 9a", "6 %",
		            "Nothing Phone 3a Pro", "3 %",
		            "Poco F7 Ultra", "6 %",
		            "Honor 400 Pro", "7 %",
		            "Apple iPhone 16e", "3 %"
		        )
		    ),
		    new TestMetricRow(
		        "30 min recharge (included charger)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "47 %",
		            "Poco F7 Ultra", "98 %",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "–"
		        )
		    ),
		    new TestMetricRow(
		        "30 min recharge (no charger included)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "34 %",
		            "Google Pixel 9a", "40 %",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "77 %",
		            "Apple iPhone 16e", "55 %"
		        )
		    ),
		    new TestMetricRow(
		        "3D Mark – Wild Life",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "4985",
		            "Google Pixel 9a", "2597",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "6023",
		            "Honor 400 Pro", "4614",
		            "Apple iPhone 16e", "2939"
		        )
		    ),
		    new TestMetricRow(
		        "Antutu benchmark test",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "–"
		        )
		    ),
		    new TestMetricRow(
		        "Battery drain 60 min (music streaming offline)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "1 %"
		        )
		    ),
		    new TestMetricRow(
		        "Battery drain 60 min (music streaming online)",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "1 %"
		        )
		    ),
		    new TestMetricRow(
		        "Geekbench 6 multi core",
		        Map.of(
		            "Xiaomi 14T Pro", "7227",
		            "OnePlus 13R", "6357",
		            "Google Pixel 9a", "3801",
		            "Nothing Phone 3a Pro", "3281",
		            "Poco F7 Ultra", "8033",
		            "Honor 400 Pro", "6519",
		            "Apple iPhone 16e", "7973"
		        )
		    ),
		    new TestMetricRow(
		        "Geekbench 6 single core",
		        Map.of(
		            "Xiaomi 14T Pro", "2226",
		            "OnePlus 13R", "2185",
		            "Google Pixel 9a", "1652",
		            "Nothing Phone 3a Pro", "1157",
		            "Poco F7 Ultra", "2283",
		            "Honor 400 Pro", "2116",
		            "Apple iPhone 16e", "3311"
		        )
		    ),
		    new TestMetricRow(
		        "GFXBench – Aztec Ruins",
		        Map.of(
		            "Xiaomi 14T Pro", "92 fps",
		            "OnePlus 13R", "60 fps",
		            "Google Pixel 9a", "66 fps",
		            "Nothing Phone 3a Pro", "25 fps",
		            "Poco F7 Ultra", "80 fps",
		            "Honor 400 Pro", "61 fps",
		            "Apple iPhone 16e", "60 fps"
		        )
		    ),
		    new TestMetricRow(
		        "GFXBench – Car Chase",
		        Map.of(
		            "Xiaomi 14T Pro", "93 fps",
		            "OnePlus 13R", "60 fps",
		            "Google Pixel 9a", "79 fps",
		            "Nothing Phone 3a Pro", "28 fps",
		            "Poco F7 Ultra", "87 fps",
		            "Honor 400 Pro", "61 fps",
		            "Apple iPhone 16e", "60 fps"
		        )
		    ),
		    new TestMetricRow(
		        "Max brightness",
		        Map.of(
		            "Xiaomi 14T Pro", "–",
		            "OnePlus 13R", "–",
		            "Google Pixel 9a", "–",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "–",
		            "Honor 400 Pro", "–",
		            "Apple iPhone 16e", "700 nits"
		        )
		    ),
		    new TestMetricRow(
		        "Time from 0 100 percent charge",
		        Map.of(
		            "Xiaomi 14T Pro", "23 min",
		            "OnePlus 13R", "90 min",
		            "Google Pixel 9a", "108 min",
		            "Nothing Phone 3a Pro", "72 min",
		            "Poco F7 Ultra", "32 min",
		            "Honor 400 Pro", "47 min",
		            "Apple iPhone 16e", "105 min"
		        )
		    ),
		    new TestMetricRow(
		        "Time from 0 50 percent charge",
		        Map.of(
		            "Xiaomi 14T Pro", "9 min",
		            "OnePlus 13R", "44 min",
		            "Google Pixel 9a", "40 min",
		            "Nothing Phone 3a Pro", "–",
		            "Poco F7 Ultra", "12 min",
		            "Honor 400 Pro", "17 min",
		            "Apple iPhone 16e", "27 min"
		        )
		    )
		);
  List<SpecRow> fullSpecs = List.of(
	        new SpecRow(
	            "ASIN",
	            Map.of(
	                "Xiaomi 14T Pro Review", "B0D6NMDXY7",
	                "OnePlus 13R Review", "–",
	                "Google Pixel 9a Review", "B0DSWFHTL2",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "B0DSG6G62L"
	            )
	        ),
	        new SpecRow(
	            "Battery",
	            Map.of(
	                "Xiaomi 14T Pro Review", "5000 mAh",
	                "OnePlus 13R Review", "6000 mAh",
	                "Google Pixel 9a Review", "5100 mAh",
	                "Nothing Phone 3a Pro Review", "5000 mAh",
	                "Poco F7 Ultra Review", "5300 mAh"
	            )
	        ),
	        new SpecRow(
	            "CA RRP",
	            Map.of(
	                "Xiaomi 14T Pro Review", "–",
	                "OnePlus 13R Review", "–",
	                "Google Pixel 9a Review", "–",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "–"
	            )
	        ),
	        new SpecRow(
	            "Chipset",
	            Map.of(
	                "Xiaomi 14T Pro Review", "MediaTek Dimensity 9300+",
	                "OnePlus 13R Review", "Qualcomm Snapdragon 8 Gen 3",
	                "Google Pixel 9a Review", "Google Tensor G4",
	                "Nothing Phone 3a Pro Review", "Snapdragon 7s Gen 3",
	                "Poco F7 Ultra Review", "Qualcomm Snapdragon 8 Elite"
	            )
	        ),
	        new SpecRow(
	            "Colours",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Titan Black, Titan Gray, Titan Blue",
	                "OnePlus 13R Review", "Astral Trail, Nebula Noir",
	                "Google Pixel 9a Review", "Obsidian, Porcelain, Iris, Peony",
	                "Nothing Phone 3a Pro Review", "Black, Grey",
	                "Poco F7 Ultra Review", "Black, Yellow"
	            )
	        ),
	        new SpecRow(
	            "EU RRP",
	            Map.of(
	                "Xiaomi 14T Pro Review", "€799.99",
	                "OnePlus 13R Review", "–",
	                "Google Pixel 9a Review", "–",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "–"
	            )
	        ),
	        new SpecRow(
	            "Fast Charging",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Yes",
	                "OnePlus 13R Review", "Yes",
	                "Google Pixel 9a Review", "–",
	                "Nothing Phone 3a Pro Review", "Yes",
	                "Poco F7 Ultra Review", "Yes"
	            )
	        ),
	        new SpecRow(
	            "First Reviewed Date",
	            Map.of(
	                "Xiaomi 14T Pro Review", "25/09/2024",
	                "OnePlus 13R Review", "10/01/2025",
	                "Google Pixel 9a Review", "10/04/2025",
	                "Nothing Phone 3a Pro Review", "04/03/2025",
	                "Poco F7 Ultra Review", "27/03/2025"
	            )
	        ),
	        new SpecRow(
	            "Front Camera",
	            Map.of(
	                "Xiaomi 14T Pro Review", "32MP",
	                "OnePlus 13R Review", "16MP",
	                "Google Pixel 9a Review", "13MP",
	                "Nothing Phone 3a Pro Review", "50MP",
	                "Poco F7 Ultra Review", "32MP"
	            )
	        ),
	        new SpecRow(
	            "HDR",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Yes",
	                "OnePlus 13R Review", "Yes",
	                "Google Pixel 9a Review", "Yes",
	                "Nothing Phone 3a Pro Review", "Yes",
	                "Poco F7 Ultra Review", "Yes"
	            )
	        ),
	        new SpecRow(
	            "IP rating",
	            Map.of(
	                "Xiaomi 14T Pro Review", "IP68",
	                "OnePlus 13R Review", "IP65",
	                "Google Pixel 9a Review", "IP68",
	                "Nothing Phone 3a Pro Review", "Not Disclosed",
	                "Poco F7 Ultra Review", "IP68"
	            )
	        ),
	        new SpecRow(
	            "Manufacturer",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Xiaomi",
	                "OnePlus 13R Review", "OnePlus",
	                "Google Pixel 9a Review", "Google",
	                "Nothing Phone 3a Pro Review", "Nothing",
	                "Poco F7 Ultra Review", "Xiaomi"
	            )
	        ),
	        new SpecRow(
	            "Operating System",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Android 14 (HyperOS)",
	                "OnePlus 13R Review", "OxygenOS 15 (Android 15)",
	                "Google Pixel 9a Review", "Android 15",
	                "Nothing Phone 3a Pro Review", "Android 15",
	                "Poco F7 Ultra Review", "HyperOS 2 (Android 15)"
	            )
	        ),
	        new SpecRow(
	            "Ports",
	            Map.of(
	                "Xiaomi 14T Pro Review", "USB C",
	                "OnePlus 13R Review", "USB C",
	                "Google Pixel 9a Review", "USB C",
	                "Nothing Phone 3a Pro Review", "USB C",
	                "Poco F7 Ultra Review", "USB C"
	            )
	        ),
	        new SpecRow(
	            "RAM",
	            Map.of(
	                "Xiaomi 14T Pro Review", "12GB, 16GB",
	                "OnePlus 13R Review", "12GB",
	                "Google Pixel 9a Review", "8GB",
	                "Nothing Phone 3a Pro Review", "12GB",
	                "Poco F7 Ultra Review", "12GB, 16GB"
	            )
	        ),
	        new SpecRow(
	            "Rear Camera",
	            Map.of(
	                "Xiaomi 14T Pro Review", "50MP + 50MP + 12MP",
	                "OnePlus 13R Review", "50MP + 50MP + 8MP",
	                "Google Pixel 9a Review", "48MP + 13MP",
	                "Nothing Phone 3a Pro Review", "50MP + 50MP + 8MP",
	                "Poco F7 Ultra Review", "50MP + 50MP + 32MP"
	            )
	        ),
	        new SpecRow(
	            "Refresh Rate",
	            Map.of(
	                "Xiaomi 14T Pro Review", "144 Hz",
	                "OnePlus 13R Review", "120 Hz",
	                "Google Pixel 9a Review", "120 Hz",
	                "Nothing Phone 3a Pro Review", "120 Hz",
	                "Poco F7 Ultra Review", "120 Hz"
	            )
	        ),
	        new SpecRow(
	            "Release Date",
	            Map.of(
	                "Xiaomi 14T Pro Review", "2024",
	                "OnePlus 13R Review", "2024",
	                "Google Pixel 9a Review", "2025",
	                "Nothing Phone 3a Pro Review", "2025",
	                "Poco F7 Ultra Review", "2025"
	            )
	        ),
	        new SpecRow(
	            "Resolution",
	            Map.of(
	                "Xiaomi 14T Pro Review", "2712 x 1220",
	                "OnePlus 13R Review", "1264 x 2780",
	                "Google Pixel 9a Review", "1080 x 2424",
	                "Nothing Phone 3a Pro Review", "2392 x 1080",
	                "Poco F7 Ultra Review", "1440 x 3200"
	            )
	        ),
	        new SpecRow(
	            "Screen Size",
	            Map.of(
	                "Xiaomi 14T Pro Review", "6.67 inches",
	                "OnePlus 13R Review", "6.78 inches",
	                "Google Pixel 9a Review", "6.3 inches",
	                "Nothing Phone 3a Pro Review", "6.77 inches",
	                "Poco F7 Ultra Review", "6.67 inches"
	            )
	        ),
	        new SpecRow(
	            "Size (Dimensions)",
	            Map.of(
	                "Xiaomi 14T Pro Review", "75.1 x 8.39 x 160.4 MM",
	                "OnePlus 13R Review", "75.8 x 8 x 161.7 MM",
	                "Google Pixel 9a Review", "73.3 x 8.9 x 154.7 MM",
	                "Nothing Phone 3a Pro Review", "77.5 x 8.39 x 163.52 MM",
	                "Poco F7 Ultra Review", "75 x 8.4 x 160.3 MM"
	            )
	        ),
	        new SpecRow(
	            "Stated Power",
	            Map.of(
	                "Xiaomi 14T Pro Review", "120 W",
	                "OnePlus 13R Review", "80 W",
	                "Google Pixel 9a Review", "23 W",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "120 W"
	            )
	        ),
	        new SpecRow(
	            "Storage Capacity",
	            Map.of(
	                "Xiaomi 14T Pro Review", "256GB, 512GB, 1TB",
	                "OnePlus 13R Review", "256GB",
	                "Google Pixel 9a Review", "128GB, 256GB",
	                "Nothing Phone 3a Pro Review", "256GB",
	                "Poco F7 Ultra Review", "256GB, 512GB"
	            )
	        ),
	        new SpecRow(
	            "UK RRP",
	            Map.of(
	                "Xiaomi 14T Pro Review", "£649",
	                "OnePlus 13R Review", "£679",
	                "Google Pixel 9a Review", "£499",
	                "Nothing Phone 3a Pro Review", "£449",
	                "Poco F7 Ultra Review", "£649"
	            )
	        ),
	        new SpecRow(
	            "USA RRP",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Unavailable",
	                "OnePlus 13R Review", "–",
	                "Google Pixel 9a Review", "$499",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "–"
	            )
	        ),
	        new SpecRow(
	            "Video Recording",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Yes",
	                "OnePlus 13R Review", "Yes",
	                "Google Pixel 9a Review", "Yes",
	                "Nothing Phone 3a Pro Review", "Yes",
	                "Poco F7 Ultra Review", "Yes"
	            )
	        ),
	        new SpecRow(
	            "Weight",
	            Map.of(
	                "Xiaomi 14T Pro Review", "209 g",
	                "OnePlus 13R Review", "206 g",
	                "Google Pixel 9a Review", "186 g",
	                "Nothing Phone 3a Pro Review", "211 g",
	                "Poco F7 Ultra Review", "212 g"
	            )
	        ),
	        new SpecRow(
	            "Wireless charging",
	            Map.of(
	                "Xiaomi 14T Pro Review", "Yes",
	                "OnePlus 13R Review", "–",
	                "Google Pixel 9a Review", "Yes",
	                "Nothing Phone 3a Pro Review", "–",
	                "Poco F7 Ultra Review", "Yes"
	            )
	        )
	    );
  

}
