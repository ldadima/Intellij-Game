<template>
  <v-dialog v-model="dialog" width="600px" persistent>
    <v-card>
      <v-card-text>
        <v-card-title class="justify-center black--text">Формулировка вопроса</v-card-title>
        <v-text-field ref="questionField" required :rules="[this.textQuestion !== '']" v-model="textQuestion" label="Введите формулировку вопроса"></v-text-field>
        <v-card-title class="justify-center black--text">Ответ</v-card-title>
        <v-text-field ref="answerField" required :rules="[this.answer !== '']" v-model="answer" label="Введите ответ"></v-text-field>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions class="justify-center">
        <v-btn color="primary" text class="text-md-h6"
               :disabled="!isFormValid"
               @click="submitForm">
          Добавить
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "EditQuestionForm",
  props: ['dialog', 'title', 'submitAction', 'question'],
  data() {
    return {
      textQuestion: '',
      answer: ''
    }
  },
  computed: {
    isFormValid() {
      return this.textQuestion !== null && this.answer !== ''
    }
  },
  methods: {
    submitForm() {
      this.submitAction(this.question, this.textQuestion, this.answer)
      this.textQuestion = ''
      this.answer = ''
      this.$refs.questionField.resetValidation()
      this.$refs.answerField.resetValidation()
    }
  },
  watch: {
    question(newVal) {
      if (newVal) {
        this.textQuestion = this.question.textQuestion
        this.answer = this.question.answer
      } else {
        this.textQuestion = ''
        this.answer = ''
      }
    }
  }
}
</script>

<style scoped>

</style>