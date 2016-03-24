(ns microblog-clojure.core
  (:require [ring.adapter.jetty :as j]
            [ring.middleware.params :as p]
            [ring.util.response :as r]
            [compojure.core :as c]
            [hiccup.core :as h])
  (:gen-class))

(c/defroutes app
  (c/GET "/" request
    (h/html [:html
             [:body
              [:form {:action "/add-message" :method "post"}
               [:input {:type "text" :placeholder "Enter Message" :name "message"}]
               [:button {:type "submit"} "Add Message"]]]]))
  (c/POST "/add-message" request
    (let [message (get (:params request) "message")]
      (println message)
      (r/redirect "/"))))
              

(defonce server (atom nil))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty (p/wrap-params app) {:port 3000 :join? false})))
  
