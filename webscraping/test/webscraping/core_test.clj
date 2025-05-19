(ns webscraping.core-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str] ;; Added for string checks
            [webscraping.core :refer :all])
  (:import (org.jsoup Jsoup))) ;; Added Jsoup import

(deftest mark-watson-website-test
  (testing "Fetch Mark Watson's website and verify basic anchor extraction"
    (let [page-data (fetch-web-page-data "https://markwatson.com")]
      (is (string? (:page-text page-data)))
      (is (not (str/blank? (:page-text page-data))))
      (is (vector? (:anchors page-data)))
      (is (pos? (count (:anchors page-data))))
      (let [anchors (:anchors page-data)]
        (is (some #(str/includes? (:text %) "Read My Blog on Blogspot") anchors)
            "Expected to find an anchor with 'Read My Blog on Blogspot' in its text")
        (is (some #(= (:uri %) "https://mark-watson.blogspot.com/") anchors)
            "Expected to find an anchor linking to https://mark-watson.blogspot.com/")

        (is (some #(str/includes? (str/lower-case (:text %)) "clojure") anchors)
            "Expected to find an anchor with 'Clojure' (case-insensitive) in its text")
        (is (some #(= (:uri %) "https://leanpub.com/clojureai") anchors)
            "Expected to find an anchor linking to Leanpub Clojure AI book")

        ;; Add a check for one more reasonably stable link, e.g., "My Books"
        (is (some #(and (str/includes? (:text %) "My Books")
                        (= (:uri %) "https://markwatson.com#books"))
                  anchors)
            "Expected to find an anchor 'My Books' linking to '#books'")))))

(deftest no-anchors-test
  (testing "Page with no anchor tags"
    (let [html-doc (Jsoup/parse "<html><body><p>No links here.</p></body></html>")
          anchors (get-html-anchors html-doc)]
      (is (empty? anchors) "Expected no anchors from HTML with no links"))))

(deftest relative-and-absolute-uris-test
  (testing "Anchor URI resolution for relative and absolute paths"
    (let [base-uri "http://example.com/docs/"
          html-content "<html><body><a href=\"/page1\">Page 1</a> <a href=\"http://domain.com/page2\">Page 2</a> <a href=\"../page3\">Page 3</a> <a href=\"sub/page4\">Page 4</a><a href=\"page5.html\">Page 5</a></body></html>"
          html-doc (. Jsoup parse html-content base-uri)
          anchors (get-html-anchors html-doc)
          uris (set (map :uri anchors))]
      (is (contains? uris "http://example.com/page1") "Relative /page1 should resolve to http://example.com/page1")
      (is (contains? uris "http://domain.com/page2") "Absolute http://domain.com/page2 should remain unchanged")
      (is (contains? uris "http://example.com/page3") "Relative ../page3 should resolve to http://example.com/page3")
      (is (contains? uris "http://example.com/docs/sub/page4") "Relative sub/page4 should resolve to http://example.com/docs/sub/page4")
      (is (contains? uris "http://example.com/docs/page5.html") "Relative page5.html should resolve to http://example.com/docs/page5.html"))))
