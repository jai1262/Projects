console.log("hello")

const express = require("express");      /** import express */
const app = express();
const mongoose = require("mongoose");
const dotenv = require("dotenv");
const userRoute = require("./routes/user");
const authRoute = require("./routes/auth");
const productRoute = require("./routes/product");
const cartRoute = require("./routes/cart");
const orderRoute = require("./routes/order");
const cors = require("cors");
//const stripeRoute = require("./routes/stripe")

dotenv.config();
const paypal = require('paypal-rest-sdk');
paypal.configure({
  'mode': 'sandbox', //sandbox or live
  'client_id': 'AY1yUxSpunOo4b3AYrbvEghOHDAAutYHXVXF5k-Zmls__LRq6sckEYDfnCK-mMea4tS0Yc-jGt0m11N_',
  'client_secret': 'ECR4AMH6KDLeF73DxzXDI36aYz6AAWN9BbJXf7zTwFWUmTUN9V-grAIbmifDmeU7YDsBfu-eQxbS3XD4'
});

mongoose.connect(process.env.MONGO_URL)
.then(() => console.log("DB Connected successfully")).catch((err) => console.log("Error\n" + err));

/*app.get("/api/test", () => {
    console.log("Test is successful");
});*/

app.use(cors({origin: true, credentials: true}));
app.use(express.json());
app.use("/api/auth", authRoute);
app.use("/api/users", userRoute);
app.use("/api/products", productRoute);
app.use("/api/carts", cartRoute);
app.use("/api/orders", orderRoute);
//app.use("/api/checkout", stripeRoute);

app.listen(process.env.PORT || 5000, () => {
    console.log("Backend server is running!");
});


