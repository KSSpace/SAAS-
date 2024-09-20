<template>
    <div class="backdrop">
        <div class="editbox">
            <div class="back">
                <button type="button" class="close" aria-label="Close" @click="closeEdit">
                    <span aria-hidden="true">X</span>
                </button>
            </div>
            <h1>编辑界面</h1>
            <p>用户ID：
            <input type="text" v-model="newUserId">
            </p>
            <p>用户名字：
            <input type="text" v-model="newUserName">
            </p>
            <p>电话号码：
            <input type="text" v-model="newPhoneNum">
            </p>
            <button @click="finishEdit">完成编辑</button>
        </div>
    </div>
    
</template>

<script>
import axios from 'axios';

export default{
    name: "Edit",
    data(){
        return{
            newUserId: this.editUser.userId,
            newUserName: this.editUser.userName,
            newPhoneNum: this.editUser.phoneNum
        }
    },
    props:['editUser', 'tenantId'],
    methods:{
        finishEdit(){
            axios.put("http://localhost:8080/api/v1/user/"+ this.tenantId + "/update_" + this.editUser.userId,{
                userId: this.newUserId,
                userName: this.newUserName,
                phoneNum: this.newPhoneNum
            })
        },
        closeEdit(){
            this.$emit('close')
        }
    },
    mounted(){
    }
}
</script>
<style>

.editbox{
    width: 400px;
    padding: 20px;
    margin: 100px auto;
    background: white;
    border-radius: 10px;
}

.editbox .close{
    float: right;
}

.backdrop{
    top: 0;
    position: fixed;
    background: rgba(0, 0, 0, 0.5);
    width: 100%;
    height: 100%;
}

</style>