new Vue({
  el: '#form',
  data: {
    inputId: undefined,
    forms: [
      //{'id': undefined, 'before': undefined, after: undefined}
    ],
  },
  methods: {
    addForm: function (id) {
      var self = this
      fetch("http://localhost:9000/json/" + id).then(function(res) {
        return res.json()
      }).then(function(json) {
        if(json.error != undefined) {
          alert(json.error)
          return
        }
        self.forms.push(
          {'id': id, 'before': json.originalValue, 'after': undefined}
        )
      })
      this.inputId = undefined
    },
    remove: function (id) {
      this.forms = this.forms.filter(f => f.id != id)
    }
  },
  computed: {
    isConfirmButtonDisabled: function () {
      if(this.forms.length == 0) return true
      var hasInvalidForm = false
      this.forms.forEach(function(e) {
        if(e.before == undefined || e.after == undefined || e.after == null || e.after == '' || e.after > 100 || e.after < 0) {
          hasInvalidForm = true
        }
      })
      if(hasInvalidForm) return true
      return false
    }
  }
})
