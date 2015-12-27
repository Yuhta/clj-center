(ns cljd
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as server])
  (:import (java.nio.file Files attribute.PosixFilePermission))
  (:gen-class))

(def ^:dynamic *nrepl-file*
  (io/file (System/getProperty "user.home") ".cljd-nrepl-port"))

(defn start-server []
  (let [nrepl-file (io/file *nrepl-file*)
        nrepl-file-str (str *nrepl-file*)
        _ (when (.exists nrepl-file)
            (throw (RuntimeException. (str "file already exists: "
                                           nrepl-file-str))))
        server (server/start-server)]
    (log/debug server)
    (spit nrepl-file (:port server))
    (try
      (Files/setPosixFilePermissions (.toPath nrepl-file)
                                     #{PosixFilePermission/OWNER_READ
                                       PosixFilePermission/OWNER_WRITE})
      (catch UnsupportedOperationException _))
    (log/info "write nrepl port info to file: " nrepl-file-str)
    (.deleteOnExit nrepl-file)
    server))
