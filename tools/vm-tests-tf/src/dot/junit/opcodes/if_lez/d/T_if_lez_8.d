.source T_if_lez_8.java
.class public dot.junit.opcodes.if_lez.d.T_if_lez_8
.super java/lang/Object


.method public <init>()V
.limit regs 1

       invoke-direct {v0}, java/lang/Object/<init>()V
       return-void
.end method

.method public run(I)Z
.limit regs 6

       if-lez v5, Label8
       const/4 v5, 0
       return v5

Label8:
       nop
       return v5
.end method
