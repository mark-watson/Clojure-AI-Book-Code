(ns webscraping.core
  (:require [clojure.string :as str]))

(import (org.jsoup Jsoup))

(defn get-html-anchors [jsoup-web-page-contents]
  (let [anchors (. jsoup-web-page-contents select "a[href]")]
    (->> anchors
         (map (fn [anchor]
                (try
                  {:text (str/trim (. anchor text))
                   :uri (. anchor absUrl "href")}
                  (catch Exception e
                    (binding [*out* *err*] (println (str "Error processing anchor: " (.getMessage e) " on page: " (. jsoup-web-page-contents title))))
                    nil))))
         (filterv some?))))

(defn fetch-web-page-data
  "Get the <a> anchor data and full text from a web URI"
  [a-uri]
  (let [doc
        (->
          (. Jsoup connect a-uri)
          (.userAgent
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.0; rv:77.0) Gecko/20100101 Firefox/77.0")
          (.timeout 20000)
          (.get))
        all-page-text (. doc text)
        anchors (get-html-anchors doc)]
    {:page-text all-page-text :anchors anchors}))

(defn -main [& args]
  (println "Fetching data from https://markwatson.com...")
  (let [page-data (fetch-web-page-data "https://markwatson.com")
        anchors (:anchors page-data)]
    (if (seq anchors)
      (do
        (println (str "\nFound " (count anchors) " anchors:"))
        (println "\nFirst 5 anchors:")
        (doseq [anchor (take 5 anchors)]
          (println (str "  Text: " (:text anchor)))
          (println (str "  URI:  " (:uri anchor)))
          (println "----")))
      (println "\nNo anchors found on the page."))
    (println "\nFetching complete.")))
