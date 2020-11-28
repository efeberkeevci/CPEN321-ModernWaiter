const{testCreateOrder, testGetUserOrder, testAddOrderedItems} = require("./test_functions")

describe("Integration test 2: ", () => {
    it("Adding items to cart", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testAddOrderedItems()
        done()
    })
})

