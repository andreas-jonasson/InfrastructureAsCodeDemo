const app = Vue.createApp({
    data() {
        return {
            pickAnswers: true,
            question: "Question text",
            option: [ "Option one", "Option two", "Option three", "Option four" ]
         }
    },
    methods: {
    checkAnswers() {
            this.pickAnswers = false
        },
    nextQuestion() {
            this.pickAnswers = true
        }
    }
})

app.mount('#quiz')