import { PayPalButtons } from "@paypal/react-paypal-js"
//import { useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router";


const PaypalCheckout = (props) => {
    //const { product } = props;
    const navigate = useNavigate();
    const cart = useSelector(state => state.cart);
    return (
        <PayPalButtons style={{
            color: "silver",
            layout: "horizontal",
            height: 48,
            tagline: false,
            label: 'pay',
        }}
        createOrder={(data, actions) => {
            return actions.order.create({
                purchase_units: [
                    {
                        description: "Your purchase",
                        amount: {
                            value: props.amt,
                        },
                    },
                ],
            });
        }}
        //if transaction is authorized then onapprove event is called
        onApprove={(data, actions) => {
            //const order = await actions.order.capture();
            return actions.order.capture().then(function (details) {
                alert(
                    "Transaction completed by" + details.payer.name.given_name
                );
                console.log(details);
                console.log(data);
                navigate("/success", {state: {
                    replace: true,
                    payData: details,
                    cart: cart,
                }});
            });
        }}/>
        //capture is a promise returned that returns the order
    )
}

export default PaypalCheckout