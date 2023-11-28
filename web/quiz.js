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
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                        'CORS' : 'eeeh' },
            body: JSON.stringify({ name: 'Vue 3 POST Request Example' })
        };

        fetch('https://iac-api.drutt.se/card')
            .then(res => res.json())
            .then(data => console.log(data))
            .catch(err => console.log(err.message))
    }
})

app.mount('#quiz')