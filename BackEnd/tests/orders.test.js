const orders = require('../routes/orders.js')

describe('checkOrder', () => {
  test('should 200', () => {
    const req = {body : { userId : "1", tableId : "1", restaurantId : "1", amount : "55.43", hasPaid : "0", isActive : "1"}}
    const res = {};

    orders.createOrder(req, res);
    expect(res.status).toEqual(200);
    expect(res.json).toEqual("");
    done()
  });
});

const mockRequest = (content) => {
  return {
    body: { content },
  };
};

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};
