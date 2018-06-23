package com.grabbd.foursquare.foursquare.RESTModels;

public interface ResponseListener {

    public abstract void onResponseRecieved(Response response);

    public static class Response {
        public boolean isOkay;
        public Object data;

        public Response(boolean isOkay, Object data) {
            this.isOkay = isOkay;
            this.data = data;
        }
    }
}
