import { useLocation } from "react-router";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { userRequest } from "../requestMethods";
import { Link } from "react-router-dom";

const Success = () => {
    const location = useLocation();
    console.log(location.state);
    const data = location.state.payData;
    const cart = location.state.cart;
    const currentUser = useSelector((state) => state.user.currentUser);
    const [orderId, setOrderId] = useState(null);

    useEffect(() => {
        const createOrder = async () => {
            try {
                const res = await userRequest.post("/orders", {
                    userId: currentUser._id,
                    products: cart.products.map((item) => ({
                        productId: item._id,
                        quantity: item._quantity,
                    }
                    )),
                    amount: cart.total,
                    address: data.payer.address.country_code,
                    status: "completed"
                });
                setOrderId(res.data._id);
            } catch {}
        };
        data && createOrder();
    }, [cart, data, currentUser]);

    return (
        <div
        style={{
            height: "100vh",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
        }}>
            {orderId
            ? `Order has been created successfully. Your order number is ${orderId}`
            : `Successful. Your order is being processed...`}
            <Link to="/">
                <button style={{ padding: 10, marginTop: 20, backgroundColor: "gray", color: "whitesmoke"}}>Go to Homepage</button>
            </Link>
        </div>
    );
};

export default Success;