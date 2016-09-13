(ns core.config
  "Configuration macros used across the project.")

(defmacro when-production
  ""
  [flag & body]
  `(when (identical? core.config/production ~flag)
     ~@body))

(defmacro if-production
  ""
  ([production-body]
   (if-production production-body nil))
  ([production-body non-production-body]
   `(if (identical? core.config/production true)
      ~production-body
      ~non-production-body)))
