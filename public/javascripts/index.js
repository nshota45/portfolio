new Vue({
  el: '#index',
  data: {
    sid: '',
    list: [], // sid, originalValue, changeValue
    isActive: false,
  },
  methods: {
    addId: function (sid) {
      var isDuplicated = false
      this.list.forEach(function(e) {
        if(e.sid == sid) isDuplicated = true
      })

      if(isDuplicated) {
        alert("idが重複しています")
        return
      }

      var self = this
      fetch("http://localhost:9000/json/" + sid).then(function(res) {
        return res.json()
      }).then(function(json) {
        if(json.error != undefined) {
          alert(json.error)
          return
        }
        self.list.push(
          {'sid': sid, 'originalValue': json.originalValue, 'changeValue': undefined}
        )
      })
      this.sid = ''
    },
    remove: function(sid) {
      this.list = this.list.filter(l => l.sid != sid)
      console.log(sid + "が削除されました")
    },
    showModal: function () {
      this.isActive = true
    },
    hideModal: function () {
    this.isActive = false}
  },
  computed: {
    isConfirmButtonDisabled: function() {
      var isAllFromValid = true
      this.list.forEach(function(e) {
        if(e.originalValue == undefined || e.changeValue == undefined || e.changeValue == null || e.changeValue == '') {
          isAllFromValid = false
        }
      })

      if (this.list.length == 0 || !isAllFromValid) {
        return true
      } else {
        return false
      }
    }
  }
})
