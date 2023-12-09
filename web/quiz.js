const app = Vue.createApp({

    data() {
        return {
            quizMode: true,
            cards: [],
            currentCard: 0,
            question: "Question",
            options: [ "Option one", "Option two", "Option three", "Option four" ],
            selected: [],
            answers: []
         }
    },

    watch: {
        cards(val) {
            this.question = val[this.currentCard].question
            this.options = val[this.currentCard].options
            this.answers = val[this.currentCard].correctOptions
        },
        currentCard(val) {
            this.question = this.cards[this.currentCard].question
            this.options = this.cards[this.currentCard].options
            this.answers = this.cards[this.currentCard].correctOptions
        }
    },

    computed: {
    },

    methods: {
        previousQuestion() {
            console.log('Previous question')
            this.currentCard--
            if (this.currentCard < 0)
                this.currentCard = this.cards.length - 1

            this.selected = []
            console.log(this.currentCard)
        },
        nextQuestion() {
            console.log('Next question')
            this.currentCard++
            if (this.currentCard >= this.cards.length)
                this.currentCard = 0

            this.selected = []
            console.log(this.currentCard)
        },
        showQuestion() {
            console.log('Show question')
            this.quizMode = true
        },
        showAnswer() {
            console.log('Show answer')
            this.quizMode = false
        },
        isCorrect(i) {
            console.log(this.selected[i])
            console.log(this.answers[i])

            if (typeof this.selected[i] === 'undefined')
                return false
            if (this.selected[i] && this.answers[i] == 1 || !this.selected[i] && this.answers[i] == 0)
                return true
            else
                return false
        }
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