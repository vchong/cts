.source "T_shr_int_2addr_6.java"
.class  public Ldot/junit/opcodes/shr_int_2addr/d/T_shr_int_2addr_6;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run(FF)I
.registers 8

       shr-int/2addr v6, v7
       return v6
.end method
