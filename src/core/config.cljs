(ns core.config
  "Configuration information used across the project.")

;; If true then application is a production environment
(goog-define production false)

;; When stringifying keywords, handle namespaces properly
(extend-type Keyword IEncodeJS (-clj->js [s] (subs (str s) 1)))
