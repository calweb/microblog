(ns microblog-clojure.core
  (:require [ring.adapter.jetty :as j]
            [ring.middleware.params :as p]
            [ring.util.response :as r]
            [compojure.core :as c]
            [hiccup.core :as h])
  (:gen-class))

(defonce messages (atom []))

(c/defroutes app
  (c/GET "/" request
    (h/html [:html
             [:body
              [:form {:action "/add-message" :method "post"}
               [:input {:type "text" :placeholder "Enter Message" :name "message"}]
               [:button {:type "submit"} "Add Message"]]
              [:ul
               (map (fn [message]
                      [:li message])
                 @messages)]]]))
  (c/POST "/add-message" request
    (let [message (get (:params request) "message")]
      (swap! messages conj message)
      (println (str @messages))
      (r/redirect "/"))))
              

(defonce server (atom nil))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty (p/wrap-params app) {:port 3000 :join? false})))
  
