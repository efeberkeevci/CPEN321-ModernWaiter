const{testCreateOrder, testGetUserOrder, testGetMenu, testGetRecommendation, testGetRecommendationInvalid } = require("./test_functions")

describe("Integration test 1: ", () => {
    it("Setting up a customer table", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testGetMenu()
        await testGetRecommendation()
        done()
    })

    it("Setting up a customer table with invalid user and restaurant id for recommendation", async done => {
        await testCreateOrder()
        await testGetUserOrder()
        await testGetMenu()
        await testGetRecommendationInvalid()
        done()
    })
})



