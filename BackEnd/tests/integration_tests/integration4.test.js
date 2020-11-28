const { 
    testTableSessionDone, testPaidStatusDone, testOrderedItemSelected, testOrderedItemPaid, 
    testGetUserOrder, testAddOrderedItems, testGetStripeKey, testCreateStripePayment, testCreateOrder
} = require("./test_functions")

const { testTableSessionDoneInvalid, testPaidStatusDoneInvalid } = require("./test_functions_invalid")


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

    it("Pay for all the items but fail to update paid and session statuses", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testAddOrderedItems()
        await testGetStripeKey()
        await testCreateStripePayment()
        await testOrderedItemPaid()
        await testTableSessionDoneInvalid()
        await testPaidStatusDoneInvalid()
        done()
    })
})


