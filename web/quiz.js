const app = Vue.createApp({

    data() {
        return {
            cards: [],
            currentCard: 0,
            question: "Question",
            options: [ "Option one", "Option two", "Option three", "Option four" ]
         }
    },

    watch: {
        cards(val) {
            this.question = val[this.currentCard].question
            this.options = val[this.currentCard].options
        }
    },

    computed: {
    },

    methods: {
    },

    mounted() {
        const request = {
            type: "GETALL",
            subject: "TEST",
            number: 0
        };

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request)
        };

        fetch('https://iac-api.drutt.se/card', requestOptions)
            .then(res => res.json())
            .then(data => this.cards = data.cards)
            .catch(err => console.log(err.message))
    }
})

app.mount('#quiz')