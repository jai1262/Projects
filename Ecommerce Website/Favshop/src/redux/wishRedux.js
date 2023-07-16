import {createSlice} from "@reduxjs/toolkit";

const wishSlice = createSlice({
    name: "wish",
    initialState: {
        products: [],
        quantity: 0,            //indicates how many items are inside our wishlist
    },
    reducers: {
        addWishProduct: (state, action) => {
            state.quantity += 1;
            state.products.push(action.payload);
            console.log(action.payload);
        },
        deleteWishProduct: (state, action) => {
            state.products = [...state.products.filter(item => (item._id !== action.payload._id))];
            state.quantity = state.products.length;
            console.log(state.products);
            console.log(action.payload._id);
            return state;
        },
    },
});

export const { addWishProduct, deleteWishProduct } = wishSlice.actions;
/*https://stackoverflow.com/questions/53766463/redux-remove-one-item-from-cart */
export default wishSlice.reducer;