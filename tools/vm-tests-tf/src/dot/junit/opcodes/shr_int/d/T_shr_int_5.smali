.source "T_shr_int_5.java"
.class  public Ldot/junit/opcodes/shr_int/d/T_shr_int_5;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run(II)I
.registers 8

       shr-int v0, v5, v7
       return v0
.end method
