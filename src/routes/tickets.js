export function ticketRoutes(app) {
    app.post('/tickets', (req, res) => {
        res.send({
            code: 200,
            message: "Success!"
        })
    })
}