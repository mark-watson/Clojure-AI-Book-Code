(ns webscraping.core
  (:require [clojure.string :as str]))

(import (org.jsoup Jsoup))

(defn get-html-anchors [jsoup-web-page-contents]
  (let [anchors (. jsoup-web-page-contents select "a[href]")]
    (for [anchor anchors]
      (try
        (let [anchor-text (. (first (. anchor childNodes)) text)
              anchor-uri-base (. (first (. anchor childNodes)) baseUri)
              href-attribute (. (. anchor attributes) get "href")
              anchor-uri
              (if (str/starts-with? href-attribute "http")
                href-attribute
                (str/join "" [anchor-uri-base (. (. anchor attributes) get "href")]))
              furi (first (. anchor childNodes))]
          {:text (str/trim anchor-text) :uri anchor-uri})
        (catch Exception e {:text (ex-message e) :uri ""})))))

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
