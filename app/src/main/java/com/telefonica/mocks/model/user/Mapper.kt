package com.telefonica.mocks.model.user

fun NameDto?.toBo(): NameBo = NameBo(
    title = this?.title ?: "",
    first = this?.first ?: "",
    last = this?.last ?: "",
)

fun UserDto?.toBo(): UserBo = UserBo(
    name = this?.name.toBo(),
    email = this?.email ?: "",
    phone = this?.phone ?: ""
)
