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
      fetch("http://localhost:9000/json").then(function(res) {
        return res.json()
      }).then(function(json) {
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
      if (this.list.length == 0) {
        return true
      } else {
        return false
      }
    }
  }
})
