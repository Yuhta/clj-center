(ns cljd-test
  (:require [cljd :refer :all]
            [clojure.test :refer :all]
            [clojure.java.shell :refer [sh]]))

(deftest test-start-server
  (with-open [_ (start-server)]
    (let [result (sh "./test.rb")]
      (is (zero? (:exit result)) (:err result)))))
