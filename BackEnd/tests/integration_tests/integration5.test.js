const{ testAddToMenu, testGetMenuLatestItem } = require("./test_functions")

describe("Integration test 5: ", () => {
    it("Add Item to Menu and verify", async done => {
        await testAddToMenu()
        await testGetMenuLatestItem()
        done()
    })
})