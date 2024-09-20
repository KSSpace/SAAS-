<template>
    <div class="backdrop">
        <div class="deletebox">
            <h1>你确定要删除此用户吗？</h1>
            <button @click="deleteUser">确认</button>
            <button @click="closeDelete">取消</button>
        </div>
    </div>

</template>
<script>
import axios from 'axios'
export default{
    name: 'Delete',
    data(){

    },
    props:['deleteUser', 'tenantId'],
    methods:{
        closeDelete(){
            this.$emit('close')
        },
        deleteUser(){
            axios.delete('http://localhost:8080/api/v1/user/'+ this.tenantId +'/delete_' + this.deleteUser.userId)
        }
    }
}
</script>
<style>
.deletebox{
    width: 400px;
    padding: 20px;
    margin: 100px auto;
    background: white;
    border-radius: 10px;
}

.deletebox h1{
    font: bold red;
}

.backdrop{
    top: 0;
    position: fixed;
    background: rgba(0, 0, 0, 0.5);
    width: 100%;
    height: 100%;
}

</style>