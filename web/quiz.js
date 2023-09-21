const app = Vue.createApp({
    // https://youtu.be/F7PLPJqVotk?list=PL4cUxeGkcC9hYYGbV60Vq3IXYNfDk8At1&t=817
    data()
     {
        return {
            question: "Oh hai!",
            option: "Yes?"
         }
    }
})

app.mount('#quiz')