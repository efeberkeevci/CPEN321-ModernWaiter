const{ testCreateOrder, testGetUserOrder, testGetTableOrder, testAddOrderedItems, testGetOrderedItems } = require("./test_functions")

describe("Integration test 3: ", () => {
    it("View bill for user", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testAddOrderedItems()
        await testGetOrderedItems()
        done()
    })

    it("View bill for table", async done => {
        await testCreateOrder()
        await testGetTableOrder()
        await testAddOrderedItems()
        await testGetOrderedItems()
        done()
    })
})