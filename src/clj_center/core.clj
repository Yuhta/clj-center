(ns clj-center.core
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :refer [start-server]])
  (:gen-class))

(def ^:dynamic *nrepl-file*
  (io/file (System/getProperty "user.home") ".clj-center-nrepl-port"))

(defn -main []
  (let [nrepl-file (io/file *nrepl-file*)
        nrepl-file-str (str *nrepl-file*)
        _ (when (.exists nrepl-file)
            (throw (RuntimeException. (str "file already exists: "
                                           nrepl-file-str))))
        server (start-server)]
    (log/debug server)
    (spit nrepl-file (:port server))
    (log/info "write nrepl port info to file: " nrepl-file-str)
    (.deleteOnExit nrepl-file)))
