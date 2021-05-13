(ns webscraping.core)

(import (org.jsoup Jsoup))
(import (org.jsoup.nodes Document))
(import (org.jsoup.nodes Element))
(import (org.jsoup.select Elements))

(defn -main
  "I don't do a whole lot."
  [& _]
  (let [doc (. Jsoup connect "https://markwatson.com")
        _ (. doc userAgent
             "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.0; rv:77.0) Gecko/20100101 Firefox/77.0")
        _ (. doc timeout 20000)
        web-page-contents (. doc get)
        news-headlines (. web-page-contents select "div p")
        headlines-text (map (fn [x] (.text x)) news-headlines)
        all_page_text (. web-page-contents text)
        anchors (. web-page-contents select "a[href]")
        anchor-1 (first anchors)
        anchor-1-text (. (first (. anchor-1 childNodes)) text)
        anchor-1-uri (. (first (. anchor-1 childNodes)) baseUri)
        ;a1-cnode (. text (first (. anchor-1 childNodes)))
        ;a1-uri (. baseUri (first (. anchor-1 childNodes)))
        ]
    ;;(println web-page-contents)
    (println news-headlines)
    (println headlines-text)
    (println all_page_text)
    (println anchors)
    (println anchor-1)
    (println anchor-1-text)
    ;(println a1-cnode)
    ;(println a1-uri)
    ))
