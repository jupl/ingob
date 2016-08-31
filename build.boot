(set-env!
 :source-paths #{"src"}
 :resource-paths #{"resources"}
 :dependencies '[[adzerk/boot-cljs              "1.7.228-1" :scope "test"]
                 [adzerk/boot-reload            "0.4.12"    :scope "test"]
                 [binaryage/devtools            "0.8.1"     :scope "test"]
                 [binaryage/dirac               "0.6.3"     :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.2.1"     :scope "test"]
                 [devcards                      "0.2.1-7"   :scope "test" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [org.clojure/clojure           "1.8.0"     :scope "test"]
                 [powerlaces/boot-cljs-devtools "0.1.1"     :scope "test"]
                 [pandeiro/boot-http            "0.7.3"     :scope "test"]
                 [tolitius/boot-check           "0.1.3"     :scope "test"]
                 [org.clojure/clojurescript     "1.9.216"]
                 [re-frame                      "0.8.0"]
                 [reagent                       "0.6.0-rc"]])

(require
 '[adzerk.boot-cljs              :refer [cljs]]
 '[adzerk.boot-reload            :refer [reload]]
 '[crisptrutski.boot-cljs-test   :refer [test-cljs]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools]]
 '[pandeiro.boot-http            :refer [serve]]
 '[tolitius.boot-check           :as    check])

;; Required to define custom test task.
(ns-unmap 'boot.user 'test)

(def closure-opts
  "Atom containing Closure Compiler options that may vary by build."
  (atom {:devcards true :output-wrapper :true}))

(def target-path
  "Default directory for build output."
  "target")

;; Define default task options used across the board.
(task-options! reload {:on-jsload 'core.reload/handle}
               serve {:dir target-path}
               target {:dir #{target-path}}
               test-cljs {:js-env :phantom})

(deftask build
  "Produce a production build with optimizations. Devcards is not included."
  []
  (swap! closure-opts assoc-in [:closure-defines 'core.config/production] true)
  (comp
   (speak)
   (sift :include #{#"^devcards"} :invert true)
   (cljs :optimizations :advanced
         :compiler-options @closure-opts)
   (sift :include #{#"\.out" #"\.cljs\.edn$" #"^\." #"/\."} :invert true)
   (target)))

(deftask dev
  "Run a local server with development tools, live updates, and devcards."
  [d no-devcards bool "Flag to indicate whether devcards should be excluded."
   p port PORT   int  "The port number to start the server in."]
  (comp
   (serve :port port)
   (watch)
   (speak)
   (if no-devcards
     (sift :include #{#"^devcards"} :invert true)
     identity)
   (reload)
   (cljs-devtools)
   (cljs :source-map true
         :optimizations :none
         :compiler-options @closure-opts)
   (sift :include #{#"\.cljs\.edn$"} :invert true)
   (target)))

(deftask devcards
  "Produce a build containing devcards only with optimizations."
  []
  (comp
   (speak)
   (sift :include #{#"^index"} :invert true)
   (cljs :optimizations :advanced
         :compiler-options @closure-opts)
   (sift :include #{#"\.out" #"\.cljs\.edn$" #"^\." #"/\."} :invert true)
   (target)))

(deftask analyze
  "Check and analyze source code."
  []
  (comp
   (sift :include #{#"\.clj(s|c)$"})
   (check/with-yagni)
   (check/with-eastwood)
   (check/with-kibit)
   (check/with-bikeshed)))

(deftask test
  "Run all tests once."
  []
  (comp
   (speak)
   (test-cljs)))
