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
            this.currentCard--
            if (this.currentCard < 0)
                this.currentCard = this.cards.length - 1

            this.selected = []
            this.quizMode = true
        },
        nextQuestion() {
            this.currentCard++
            if (this.currentCard >= this.cards.length)
                this.currentCard = 0

            this.selected = []
            this.quizMode = true
        },
        showQuestion() {
            this.quizMode = true
        },
        showAnswer() {
            this.quizMode = false
        },
        isCorrect(i) {
            if (typeof this.selected[i] === 'undefined')
                this.selected[i] = false
            else
                return this.selected[i] == this.answers[i]
        },
        allCorrect()
        {
            for (let i = 0; i < 5; i++)
            {
                if (typeof this.selected[i] === 'undefined')
                    this.selected[i] = false
                if (typeof this.answers[i] === 'undefined')
                    return true
                if (this.selected[i] != this.answers[i])
                    return false
            }
            return true;
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