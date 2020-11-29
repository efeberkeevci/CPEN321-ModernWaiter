const { testCreateOrder, testGetUserOrder, testGetMenu, testGetRecommendation, testGetTableOrder } = require("./test_functions")
const { testCreateOrderInvalid, testGetRecommendationInvalid, testGetUserOrderInvalid, testGetTableOrderInvalid } = require("./test_functions_invalid")

describe("Integration test 1: ", () => {
    it("Setting up a customer table", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testGetTableOrder()
        await testGetMenu()
        await testGetRecommendation()
        done()
    })

    it("Setting up a customer table with invalid user and restaurant id", async done => {
        await testCreateOrderInvalid()
        await testGetUserOrderInvalid()
        await testGetTableOrderInvalid()
        await testGetMenu()
        await testGetRecommendationInvalid()
        done()
    })
})



