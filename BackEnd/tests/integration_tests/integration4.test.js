const{ testTableSessionDone, testPaidStatusDone, testOrderedItemSelected, testOrderedItemPaid, testGetUserOrder, testAddOrderedItems, testGetStripeKey, testCreateStripePayment, testCreateOrder} = require("./test_functions")

describe("Integration test 4: ", () => {
    it("User selects the items to be paid", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testAddOrderedItems()
        await testOrderedItemSelected()
        done()
    })

    it("Pay for all the items", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testAddOrderedItems()
        await testGetStripeKey()
        await testCreateStripePayment()
        await testOrderedItemPaid()
        await testTableSessionDone()
        await testPaidStatusDone()
        done()
    })
})


