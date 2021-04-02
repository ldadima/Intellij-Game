<template>
  <v-layout column>
    <v-data-table
        no-data-text="Добавьте вопросы"
        :headers="headers"
        :items="questions"
        :hide-default-footer="true"
    >
      <template #item.actions="{ item }">
        <v-icon small class="mr-2" @click="editQuestion(item)">mdi-pencil</v-icon>
        <v-icon small class="mr-2" @click="deleteQuestion(item)">mdi-delete</v-icon>
      </template>
    </v-data-table>
    <v-btn @click="createQuestion">Добавить</v-btn>
    <edit-question-form
        :dialog="formDialog"
        :title="formTitle"
        :submit-action="formAction"
        :question="formQuestion"
    ></edit-question-form>
  </v-layout>
</template>

<script>
import EditQuestionForm from './EditQuestionForm.vue'
export default {
  name: "QuestionsList",
  components: {EditQuestionForm},
  props: ['questions'],
  data() {
    return {
      headers: [
        {text:'Вопрос', value:'numberInGame', sortable: false},
        {text: 'Формулировка вопроса', value:'textQuestion', sortable:false},
        {text: 'Ответ', value:'answer', sortable: false},
        {text: 'Номер раунда', value: 'numberLap', sortable: false},
        {text: '', value: 'actions', sortable: false}
      ],
      formDialog: false,
      formTitle: '',
      formAction: null,
      formQuestion: {}
    }
  },
  methods: {
    editQuestion(item) {
      this.formTitle = 'Редактирование вопроса'
      this.formAction = this.submitEdit
      this.formQuestion = item
      this.formDialog = true
    },
    submitEdit(oldQuestion, newTextQuestion, newAnswer) {
      this.formDialog = false
      const index = this.questions.indexOf(oldQuestion)
      const newQuestion = {
        id: oldQuestion.id,
        textQuestion: newTextQuestion,
        numberInGame: oldQuestion.numberInGame,
        answer: newAnswer,
        numberLap: oldQuestion.numberLap
      }
      this.questions.splice(index, 1, newQuestion)
    },
    deleteQuestion(item) {
      const index = this.questions.indexOf(item)
      this.questions.splice(index, 1)
    },
    createQuestion() {
      this.formTitle = 'Добавление вопроса'
      this.formAction = this.submitCreate
      this.formDialog = true
      this.formQuestion = null
    },
    submitCreate(oldQuestion, newTextQuestion, newAnswer) {
      this.formDialog = false
      const newQuestion = {
        textQuestion: newTextQuestion,
        answer: newAnswer
      }
      this.questions.push(newQuestion)
    }
  }
}
</script>

<style scoped>

</style>