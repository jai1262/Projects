import {createSlice} from "@reduxjs/toolkit";

const cartSlice = createSlice({
    name: "cart",
    initialState: {
        products: [],
        quantity: 0,            //indicates how many items are inside our cart
        total: 0,
    },
    reducers: {
        addProduct: (state, action) => {
            state.quantity += 1;
            state.products.push(action.payload);
            state.total += action.payload.price * action.payload.count;
            console.log(action.payload);
            console.log(state.total);
        },
        modifyCount: (state, action) => {
            let updateCart = state.products.map((item) => {
                if (item._id === action.payload.id) {
                    if (action.payload.mod === "inc") {
                        state.total += item.price
                        return {
                            ...item,
                            count: item.count + 1
                        }
                    } else {
                        state.total -= item.price
                        return {
                            ...item,
                            count: item.count - 1
                        }
                    }
                } else {
                    return {...item}
                }
            })
            //updateCart = [...updateCart.filter(item => item.count > 0)]
            //console.log(updateCart);
            //console.log(tot);
            state.products = [...updateCart.filter(item => item.count > 0)]
            state.quantity = state.products.length;
        },
        deleteProduct: (state, action) => {
            state.products = [...state.products.filter(item => (item._id !== action.payload._id))];
            state.quantity -= 1;
            state.total -= action.payload.price * action.payload.count;
            console.log(state.products);
            console.log(action.payload._id);
            return state;
        },
        emptyCart: (state, action) => {
            state.products = [];
            state.quantity = 0;
            state.total = 0;
        }
    },
});

export const { addProduct, modifyCount, deleteProduct, emptyCart } = cartSlice.actions;
/*https://stackoverflow.com/questions/53766463/redux-remove-one-item-from-cart */
export default cartSlice.reducer;