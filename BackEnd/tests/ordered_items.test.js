const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)

//Tests for GET /ordered-items/{{orderId}}
describe("Test getOrderedItems()", ()=>{

    //Testing Invalid orderId type
    test("Testing Invalid orderId type", async done=>{

        //Arrange
        let param = "Invalid orderId of wrong data type"
        let path = "/ordered-items/"
        
        //Act
        const res = await request.get(path+param)

        //Assert
        expect(res.statusCode).toEqual(400)
        done()
    })
    
    //Testing valid orderId type
    test("Testing valid orderId type", async done=>{

        //Arrange
        let param = "1"
        let path = "/ordered-items/"
        
        //Act
        const res = await request.get(path+param)

        //Assert
        //TODO: What to do for the case query returns 400 although valid
        expect(res.statusCode).toEqual(200)
        expect(typeof response.body[0].id).toBe('number')
        expect(typeof response.body[0].orders_id).toBe('number')
        expect(typeof response.body[0].items_id).toBe('number')
        expect(typeof response.body[0].has_paid).toBe('number')
        expect(typeof response.body[0].is_selected).toBe('number')
        expect(typeof response.body[0].users_id).toBe('number')
        done()
    })

})

describe("Test addOrderedItems()",()=>{

})

describe("Test updateSelectedStatus()",() =>{

})

describe("Test updateOrderedItemPaidStatus()", () =>{

})
