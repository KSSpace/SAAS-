<template>
  <div class="edit" v-if="showEdit">
    <Edit :editUser="this.editUser" :tenantId="this.tenantId" @close="toggleEndEdit"/>
  </div>
  <div class="delete" v-if="showDelete">
    <Delete :deleteUser="this.deleteUser" :tenantId="this.tenantId" @close="toggleEndDelete"/>
  </div>
  <div class>
    <div class="header">
      <h1>{{ this.title }}</h1>
      <h2>{{ this.desc }}</h2>
    </div>
    <div class="home">
      <button @click="toggleAllUsers">主页</button>
    </div>
    <div class="search">
      <input type="text" placeholder="用户ID" v-model="userId">
      <button @click="toggleSearch">查询</button>
    </div>
    <div class="container">
      <div class="formShowAll" v-if="showAllUsers">
        <tr>
          <td>用户ID</td>
          <td>用户名字</td>
          <td>编辑</td>
          <td>删除</td>
        </tr>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.userId }}</td>
          <td>{{ user.userName }}</td>
          <td><button @click="toggleEdit(user)">编辑</button></td>
          <td><button @click="toggleDelete">删除</button></td>
        </tr>
      </div>
      <div class="formShowOne" v-if="user && !showAllUsers">
        <tr>
          <td>用户ID</td>
          <td>用户名字</td>
          <td>编辑</td>
          <td>删除</td>
        </tr>
        <tr>
          <td>{{ user.userId }}</td>
          <td>{{ user.userName }}</td>
          <td><button @click="toggleEdit(user)">编辑</button></td>
          <td><button @click="toggleDelete">删除</button></td>
        </tr>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import Edit from './components/Edit.vue'
import Delete from './components/Delete.vue'
export default {
  name: 'App',
  components:{
    Edit, Delete
  },
  data(){
    return{
      title: "用户查询",
      desc: "这是租户的用户查询界面",
      users: [],
      tenantId: "653680d6de5a5f324f1392a6",
      user: null,
      showAllUsers: true,
      userId: null,
      showEdit: false,
      editUser: null,
      showDelete: false,
      deleteUser: null
    }
  },
  methods:{
    toggleSearch(){
      this.showAllUsers = false
      axios.get('http://localhost:8080/api/v1/user/' + this.tenantId + '/' + this.userId)
      .then(res => {
        console.log(res)
        this.user = res.data
      })
      .catch(err => console.log(err.message))
    },
    toggleAllUsers(){
      this.showAllUsers = true
    },
    toggleEdit(user){
      this.showEdit = true
      this.editUser = user
    },
    toggleEndEdit(){
      this.showEdit = false
      axios.get('http://localhost:8080/api/v1/user/' + this.tenantId)
      .then(res => {
        console.log(res)
        this.users = res.data
      })
      .catch(err => console.log(err.message))
    },
    toggleDelete(){
      this.showDelete = true

    },
    toggleEndDelete(){
      this.showDelete = false
    }
  },
  mounted(){
    axios.get('http://localhost:8080/api/v1/user/' + this.tenantId)
    .then(res => {
      console.log(res)
      this.users = res.data
    })
    .catch(err => console.log(err.message))
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.header{
  width: 100%;
}

.search{
  width: 100%;
}

.container{
  width: 100%;
}

.container tr{
  padding: 10px;
  display:flex;
  align-items: center;
  justify-content: space-evenly;
}

</style>
