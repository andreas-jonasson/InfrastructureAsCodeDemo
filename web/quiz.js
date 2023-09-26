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
    },

    mounted() {
        fetch('endpoint')
            .then(res => res.json())
            .then(data => this.questions = data)
            .catch(err => console.log(err.message))
    }
})

app.mount('#quiz')