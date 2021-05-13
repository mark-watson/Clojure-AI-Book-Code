(ns webscraping.core
  (:require [clojure.string :as str]))

(import (org.jsoup Jsoup))
(import (org.jsoup.nodes Document))
(import (org.jsoup.nodes Element))
(import (org.jsoup.select Elements))

(defn get-html-anchors [jsoup-web-page-contents]
  (let [anchors (. jsoup-web-page-contents select "a[href]")]
    (for [anchor anchors]
      (let [anchor-text (. (first (. anchor childNodes)) text)
            anchor-uri (. (first (. anchor childNodes)) baseUri)]
        {:text (str/trim anchor-text) :uri anchor-uri}))))

(defn -main
  "I don't do a whole lot."
  [& _]
  (let [doc
        (->
          (. Jsoup connect "https://markwatson.com")
          (.userAgent
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.0; rv:77.0) Gecko/20100101 Firefox/77.0")
          (.timeout 20000)
          (.get))
        all-page-text (. doc text)
        anchors (get-html-anchors doc)]
    ;;(println web-page-contents)
    (println all-page-text)
    (println anchors)
    (println 'done)
    {:page-text all-page-text :anchors anchors}))

