create table ACT_GE_BYTEARRAY
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	NAME_ NVARCHAR2(255),
	DEPLOYMENT_ID_ NVARCHAR2(64),
	BYTES_ BLOB,
	GENERATED_ NUMBER(1)
		check (GENERATED_ IN (1,0))
)
/

create index ACT_IDX_BYTEAR_DEPL
	on ACT_GE_BYTEARRAY (DEPLOYMENT_ID_)
/

create table ACT_GE_PROPERTY
(
	NAME_ NVARCHAR2(64) not null
		primary key,
	VALUE_ NVARCHAR2(300),
	REV_ NUMBER
)
/

create table ACT_RU_IDENTITYLINK
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	GROUP_ID_ NVARCHAR2(255),
	TYPE_ NVARCHAR2(255),
	USER_ID_ NVARCHAR2(255),
	TASK_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255)
)
/

create index ACT_IDX_IDENT_LNK_GROUP
	on ACT_RU_IDENTITYLINK (GROUP_ID_)
/

create index ACT_IDX_IDENT_LNK_USER
	on ACT_RU_IDENTITYLINK (USER_ID_)
/

create index ACT_IDX_TSKASS_TASK
	on ACT_RU_IDENTITYLINK (TASK_ID_)
/

create index ACT_IDX_IDL_PROCINST
	on ACT_RU_IDENTITYLINK (PROC_INST_ID_)
/

create index ACT_IDX_ATHRZ_PROCEDEF
	on ACT_RU_IDENTITYLINK (PROC_DEF_ID_)
/

create index ACT_IDX_IDENT_LNK_SCOPE
	on ACT_RU_IDENTITYLINK (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_IDENT_LNK_SCOPE_DEF
	on ACT_RU_IDENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create table ACT_RU_TASK
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	EXECUTION_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	TASK_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	NAME_ NVARCHAR2(255),
	PARENT_TASK_ID_ NVARCHAR2(64),
	DESCRIPTION_ NVARCHAR2(2000),
	TASK_DEF_KEY_ NVARCHAR2(255),
	OWNER_ NVARCHAR2(255),
	ASSIGNEE_ NVARCHAR2(255),
	DELEGATION_ NVARCHAR2(64),
	PRIORITY_ NUMBER,
	CREATE_TIME_ TIMESTAMP(6),
	DUE_DATE_ TIMESTAMP(6),
	CATEGORY_ NVARCHAR2(255),
	SUSPENSION_STATE_ NUMBER,
	TENANT_ID_ NVARCHAR2(255) default '',
	FORM_KEY_ NVARCHAR2(255),
	CLAIM_TIME_ TIMESTAMP(6),
	IS_COUNT_ENABLED_ NUMBER(1)
		check (IS_COUNT_ENABLED_ IN (1,0)),
	VAR_COUNT_ NUMBER,
	ID_LINK_COUNT_ NUMBER,
	SUB_TASK_COUNT_ NUMBER
)
/

create index ACT_IDX_TASK_EXEC
	on ACT_RU_TASK (EXECUTION_ID_)
/

create index ACT_IDX_TASK_PROCINST
	on ACT_RU_TASK (PROC_INST_ID_)
/

create index ACT_IDX_TASK_PROCDEF
	on ACT_RU_TASK (PROC_DEF_ID_)
/

create index ACT_IDX_TASK_SCOPE
	on ACT_RU_TASK (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TASK_SUB_SCOPE
	on ACT_RU_TASK (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TASK_SCOPE_DEF
	on ACT_RU_TASK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TASK_CREATE
	on ACT_RU_TASK (CREATE_TIME_)
/

alter table ACT_RU_IDENTITYLINK
	add constraint ACT_FK_TSKASS_TASK
		foreign key (TASK_ID_) references ACT_RU_TASK
/

create table ACT_RU_VARIABLE
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	TYPE_ NVARCHAR2(255) not null,
	NAME_ NVARCHAR2(255) not null,
	EXECUTION_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	TASK_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	BYTEARRAY_ID_ NVARCHAR2(64)
		constraint ACT_FK_VAR_BYTEARRAY
			references ACT_GE_BYTEARRAY,
	DOUBLE_ NUMBER(*,10),
	LONG_ NUMBER(19),
	TEXT_ NVARCHAR2(2000),
	TEXT2_ NVARCHAR2(2000)
)
/

create index ACT_IDX_VAR_EXE
	on ACT_RU_VARIABLE (EXECUTION_ID_)
/

create index ACT_IDX_VAR_PROCINST
	on ACT_RU_VARIABLE (PROC_INST_ID_)
/

create index ACT_IDX_VARIABLE_TASK_ID
	on ACT_RU_VARIABLE (TASK_ID_)
/

create index ACT_IDX_RU_VAR_SCOPE_ID_TYPE
	on ACT_RU_VARIABLE (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_RU_VAR_SUB_ID_TYPE
	on ACT_RU_VARIABLE (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_VAR_BYTEARRAY
	on ACT_RU_VARIABLE (BYTEARRAY_ID_)
/

create table ACT_RU_DEADLETTER_JOB
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	TYPE_ NVARCHAR2(255) not null,
	EXCLUSIVE_ NUMBER(1)
		check (EXCLUSIVE_ IN (1,0)),
	EXECUTION_ID_ NVARCHAR2(64),
	PROCESS_INSTANCE_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	EXCEPTION_STACK_ID_ NVARCHAR2(64)
		constraint ACT_FK_DJOB_EXCEPTION
			references ACT_GE_BYTEARRAY,
	EXCEPTION_MSG_ NVARCHAR2(2000),
	DUEDATE_ TIMESTAMP(6),
	REPEAT_ NVARCHAR2(255),
	HANDLER_TYPE_ NVARCHAR2(255),
	HANDLER_CFG_ NVARCHAR2(2000),
	CUSTOM_VALUES_ID_ NVARCHAR2(64)
		constraint ACT_FK_DJOB_CUSTOM_VAL
			references ACT_GE_BYTEARRAY,
	CREATE_TIME_ TIMESTAMP(6),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_DJOB_EXECUTION_ID
	on ACT_RU_DEADLETTER_JOB (EXECUTION_ID_)
/

create index ACT_IDX_DJOB_PROC_INST_ID
	on ACT_RU_DEADLETTER_JOB (PROCESS_INSTANCE_ID_)
/

create index ACT_IDX_DJOB_PROC_DEF_ID
	on ACT_RU_DEADLETTER_JOB (PROC_DEF_ID_)
/

create index ACT_IDX_DJOB_SCOPE
	on ACT_RU_DEADLETTER_JOB (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_DJOB_SUB_SCOPE
	on ACT_RU_DEADLETTER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_DJOB_SCOPE_DEF
	on ACT_RU_DEADLETTER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_DJOB_EXCEPTION
	on ACT_RU_DEADLETTER_JOB (EXCEPTION_STACK_ID_)
/

create index ACT_IDX_DJOB_CUSTOM_VAL_ID
	on ACT_RU_DEADLETTER_JOB (CUSTOM_VALUES_ID_)
/

create table ACT_RU_HISTORY_JOB
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	LOCK_EXP_TIME_ TIMESTAMP(6),
	LOCK_OWNER_ NVARCHAR2(255),
	RETRIES_ NUMBER,
	EXCEPTION_STACK_ID_ NVARCHAR2(64),
	EXCEPTION_MSG_ NVARCHAR2(2000),
	HANDLER_TYPE_ NVARCHAR2(255),
	HANDLER_CFG_ NVARCHAR2(2000),
	CUSTOM_VALUES_ID_ NVARCHAR2(64),
	ADV_HANDLER_CFG_ID_ NVARCHAR2(64),
	CREATE_TIME_ TIMESTAMP(6),
	SCOPE_TYPE_ NVARCHAR2(255),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create table ACT_RU_JOB
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	TYPE_ NVARCHAR2(255) not null,
	LOCK_EXP_TIME_ TIMESTAMP(6),
	LOCK_OWNER_ NVARCHAR2(255),
	EXCLUSIVE_ NUMBER(1)
		check (EXCLUSIVE_ IN (1,0)),
	EXECUTION_ID_ NVARCHAR2(64),
	PROCESS_INSTANCE_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	RETRIES_ NUMBER,
	EXCEPTION_STACK_ID_ NVARCHAR2(64)
		constraint ACT_FK_JOB_EXCEPTION
			references ACT_GE_BYTEARRAY,
	EXCEPTION_MSG_ NVARCHAR2(2000),
	DUEDATE_ TIMESTAMP(6),
	REPEAT_ NVARCHAR2(255),
	HANDLER_TYPE_ NVARCHAR2(255),
	HANDLER_CFG_ NVARCHAR2(2000),
	CUSTOM_VALUES_ID_ NVARCHAR2(64)
		constraint ACT_FK_JOB_CUSTOM_VAL
			references ACT_GE_BYTEARRAY,
	CREATE_TIME_ TIMESTAMP(6),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_JOB_EXECUTION_ID
	on ACT_RU_JOB (EXECUTION_ID_)
/

create index ACT_IDX_JOB_PROC_INST_ID
	on ACT_RU_JOB (PROCESS_INSTANCE_ID_)
/

create index ACT_IDX_JOB_PROC_DEF_ID
	on ACT_RU_JOB (PROC_DEF_ID_)
/

create index ACT_IDX_JOB_SCOPE
	on ACT_RU_JOB (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_JOB_SUB_SCOPE
	on ACT_RU_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_JOB_SCOPE_DEF
	on ACT_RU_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_JOB_EXCEPTION
	on ACT_RU_JOB (EXCEPTION_STACK_ID_)
/

create index ACT_IDX_JOB_CUSTOM_VAL_ID
	on ACT_RU_JOB (CUSTOM_VALUES_ID_)
/

create table ACT_RU_SUSPENDED_JOB
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	TYPE_ NVARCHAR2(255) not null,
	EXCLUSIVE_ NUMBER(1)
		check (EXCLUSIVE_ IN (1,0)),
	EXECUTION_ID_ NVARCHAR2(64),
	PROCESS_INSTANCE_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	RETRIES_ NUMBER,
	EXCEPTION_STACK_ID_ NVARCHAR2(64)
		constraint ACT_FK_SJOB_EXCEPTION
			references ACT_GE_BYTEARRAY,
	EXCEPTION_MSG_ NVARCHAR2(2000),
	DUEDATE_ TIMESTAMP(6),
	REPEAT_ NVARCHAR2(255),
	HANDLER_TYPE_ NVARCHAR2(255),
	HANDLER_CFG_ NVARCHAR2(2000),
	CUSTOM_VALUES_ID_ NVARCHAR2(64)
		constraint ACT_FK_SJOB_CUSTOM_VAL
			references ACT_GE_BYTEARRAY,
	CREATE_TIME_ TIMESTAMP(6),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_SJOB_EXECUTION_ID
	on ACT_RU_SUSPENDED_JOB (EXECUTION_ID_)
/

create index ACT_IDX_SJOB_PROC_INST_ID
	on ACT_RU_SUSPENDED_JOB (PROCESS_INSTANCE_ID_)
/

create index ACT_IDX_SJOB_PROC_DEF_ID
	on ACT_RU_SUSPENDED_JOB (PROC_DEF_ID_)
/

create index ACT_IDX_SJOB_SCOPE
	on ACT_RU_SUSPENDED_JOB (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_SJOB_SUB_SCOPE
	on ACT_RU_SUSPENDED_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_SJOB_SCOPE_DEF
	on ACT_RU_SUSPENDED_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_SJOB_EXCEPTION
	on ACT_RU_SUSPENDED_JOB (EXCEPTION_STACK_ID_)
/

create index ACT_IDX_SJOB_CUSTOM_VAL_ID
	on ACT_RU_SUSPENDED_JOB (CUSTOM_VALUES_ID_)
/

create table ACT_RU_TIMER_JOB
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	TYPE_ NVARCHAR2(255) not null,
	LOCK_EXP_TIME_ TIMESTAMP(6),
	LOCK_OWNER_ NVARCHAR2(255),
	EXCLUSIVE_ NUMBER(1)
		check (EXCLUSIVE_ IN (1,0)),
	EXECUTION_ID_ NVARCHAR2(64),
	PROCESS_INSTANCE_ID_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	RETRIES_ NUMBER,
	EXCEPTION_STACK_ID_ NVARCHAR2(64)
		constraint ACT_FK_TJOB_EXCEPTION
			references ACT_GE_BYTEARRAY,
	EXCEPTION_MSG_ NVARCHAR2(2000),
	DUEDATE_ TIMESTAMP(6),
	REPEAT_ NVARCHAR2(255),
	HANDLER_TYPE_ NVARCHAR2(255),
	HANDLER_CFG_ NVARCHAR2(2000),
	CUSTOM_VALUES_ID_ NVARCHAR2(64)
		constraint ACT_FK_TJOB_CUSTOM_VAL
			references ACT_GE_BYTEARRAY,
	CREATE_TIME_ TIMESTAMP(6),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_TJOB_EXECUTION_ID
	on ACT_RU_TIMER_JOB (EXECUTION_ID_)
/

create index ACT_IDX_TJOB_PROC_INST_ID
	on ACT_RU_TIMER_JOB (PROCESS_INSTANCE_ID_)
/

create index ACT_IDX_TJOB_PROC_DEF_ID
	on ACT_RU_TIMER_JOB (PROC_DEF_ID_)
/

create index ACT_IDX_TJOB_SCOPE
	on ACT_RU_TIMER_JOB (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TJOB_SUB_SCOPE
	on ACT_RU_TIMER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TJOB_SCOPE_DEF
	on ACT_RU_TIMER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_TJOB_EXCEPTION
	on ACT_RU_TIMER_JOB (EXCEPTION_STACK_ID_)
/

create index ACT_IDX_TJOB_CUSTOM_VAL_ID
	on ACT_RU_TIMER_JOB (CUSTOM_VALUES_ID_)
/

create table ACT_EVT_LOG
(
	LOG_NR_ NUMBER(19) not null
		primary key,
	TYPE_ NVARCHAR2(64),
	PROC_DEF_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	EXECUTION_ID_ NVARCHAR2(64),
	TASK_ID_ NVARCHAR2(64),
	TIME_STAMP_ TIMESTAMP(6) not null,
	USER_ID_ NVARCHAR2(255),
	DATA_ BLOB,
	LOCK_OWNER_ NVARCHAR2(255),
	LOCK_TIME_ TIMESTAMP(6),
	IS_PROCESSED_ NUMBER(3) default 0
)
/

create table ACT_RE_DEPLOYMENT
(
	ID_ NVARCHAR2(64) not null
		primary key,
	NAME_ NVARCHAR2(255),
	CATEGORY_ NVARCHAR2(255),
	KEY_ NVARCHAR2(255),
	TENANT_ID_ NVARCHAR2(255) default '',
	DEPLOY_TIME_ TIMESTAMP(6),
	DERIVED_FROM_ NVARCHAR2(64),
	DERIVED_FROM_ROOT_ NVARCHAR2(64),
	PARENT_DEPLOYMENT_ID_ NVARCHAR2(255),
	ENGINE_VERSION_ NVARCHAR2(255)
)
/

alter table ACT_GE_BYTEARRAY
	add constraint ACT_FK_BYTEARR_DEPL
		foreign key (DEPLOYMENT_ID_) references ACT_RE_DEPLOYMENT
/

create table ACT_RE_MODEL
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	NAME_ NVARCHAR2(255),
	KEY_ NVARCHAR2(255),
	CATEGORY_ NVARCHAR2(255),
	CREATE_TIME_ TIMESTAMP(6),
	LAST_UPDATE_TIME_ TIMESTAMP(6),
	VERSION_ NUMBER,
	META_INFO_ NVARCHAR2(2000),
	DEPLOYMENT_ID_ NVARCHAR2(64)
		constraint ACT_FK_MODEL_DEPLOYMENT
			references ACT_RE_DEPLOYMENT,
	EDITOR_SOURCE_VALUE_ID_ NVARCHAR2(64)
		constraint ACT_FK_MODEL_SOURCE
			references ACT_GE_BYTEARRAY,
	EDITOR_SOURCE_EXTRA_VALUE_ID_ NVARCHAR2(64)
		constraint ACT_FK_MODEL_SOURCE_EXTRA
			references ACT_GE_BYTEARRAY,
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_MODEL_DEPLOYMENT
	on ACT_RE_MODEL (DEPLOYMENT_ID_)
/

create index ACT_IDX_MODEL_SOURCE
	on ACT_RE_MODEL (EDITOR_SOURCE_VALUE_ID_)
/

create index ACT_IDX_MODEL_SOURCE_EXTRA
	on ACT_RE_MODEL (EDITOR_SOURCE_EXTRA_VALUE_ID_)
/

create table ACT_RE_PROCDEF
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	CATEGORY_ NVARCHAR2(255),
	NAME_ NVARCHAR2(255),
	KEY_ NVARCHAR2(255) not null,
	VERSION_ NUMBER not null,
	DEPLOYMENT_ID_ NVARCHAR2(64),
	RESOURCE_NAME_ NVARCHAR2(2000),
	DGRM_RESOURCE_NAME_ VARCHAR2(4000),
	DESCRIPTION_ NVARCHAR2(2000),
	HAS_START_FORM_KEY_ NUMBER(1)
		check (HAS_START_FORM_KEY_ IN (1,0)),
	HAS_GRAPHICAL_NOTATION_ NUMBER(1)
		check (HAS_GRAPHICAL_NOTATION_ IN (1,0)),
	SUSPENSION_STATE_ NUMBER,
	TENANT_ID_ NVARCHAR2(255) default '',
	DERIVED_FROM_ NVARCHAR2(64),
	DERIVED_FROM_ROOT_ NVARCHAR2(64),
	DERIVED_VERSION_ NUMBER default 0 not null,
	ENGINE_VERSION_ NVARCHAR2(255),
	constraint ACT_UNIQ_PROCDEF
		unique (KEY_, VERSION_, DERIVED_VERSION_, TENANT_ID_)
)
/

alter table ACT_RU_IDENTITYLINK
	add constraint ACT_FK_ATHRZ_PROCEDEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

alter table ACT_RU_TASK
	add constraint ACT_FK_TASK_PROCDEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

alter table ACT_RU_DEADLETTER_JOB
	add constraint ACT_FK_DJOB_PROC_DEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

alter table ACT_RU_JOB
	add constraint ACT_FK_JOB_PROC_DEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

alter table ACT_RU_SUSPENDED_JOB
	add constraint ACT_FK_SJOB_PROC_DEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

alter table ACT_RU_TIMER_JOB
	add constraint ACT_FK_TJOB_PROC_DEF
		foreign key (PROC_DEF_ID_) references ACT_RE_PROCDEF
/

create table ACT_RU_EVENT_SUBSCR
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	EVENT_TYPE_ NVARCHAR2(255) not null,
	EVENT_NAME_ NVARCHAR2(255),
	EXECUTION_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	ACTIVITY_ID_ NVARCHAR2(64),
	CONFIGURATION_ NVARCHAR2(255),
	CREATED_ TIMESTAMP(6) not null,
	PROC_DEF_ID_ NVARCHAR2(64),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_EVENT_SUBSCR
	on ACT_RU_EVENT_SUBSCR (EXECUTION_ID_)
/

create index ACT_IDX_EVENT_SUBSCR_CONFIG_
	on ACT_RU_EVENT_SUBSCR (CONFIGURATION_)
/

create table ACT_RU_EXECUTION
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	PROC_INST_ID_ NVARCHAR2(64)
		constraint ACT_FK_EXE_PROCINST
			references ACT_RU_EXECUTION,
	BUSINESS_KEY_ NVARCHAR2(255),
	PARENT_ID_ NVARCHAR2(64)
		constraint ACT_FK_EXE_PARENT
			references ACT_RU_EXECUTION,
	PROC_DEF_ID_ NVARCHAR2(64)
		constraint ACT_FK_EXE_PROCDEF
			references ACT_RE_PROCDEF,
	SUPER_EXEC_ NVARCHAR2(64)
		constraint ACT_FK_EXE_SUPER
			references ACT_RU_EXECUTION,
	ROOT_PROC_INST_ID_ NVARCHAR2(64),
	ACT_ID_ NVARCHAR2(255),
	IS_ACTIVE_ NUMBER(1)
		check (IS_ACTIVE_ IN (1,0)),
	IS_CONCURRENT_ NUMBER(1)
		check (IS_CONCURRENT_ IN (1,0)),
	IS_SCOPE_ NUMBER(1)
		check (IS_SCOPE_ IN (1,0)),
	IS_EVENT_SCOPE_ NUMBER(1)
		check (IS_EVENT_SCOPE_ IN (1,0)),
	IS_MI_ROOT_ NUMBER(1)
		check (IS_MI_ROOT_ IN (1,0)),
	SUSPENSION_STATE_ NUMBER,
	CACHED_ENT_STATE_ NUMBER,
	TENANT_ID_ NVARCHAR2(255) default '',
	NAME_ NVARCHAR2(255),
	START_ACT_ID_ NVARCHAR2(255),
	START_TIME_ TIMESTAMP(6),
	START_USER_ID_ NVARCHAR2(255),
	LOCK_TIME_ TIMESTAMP(6),
	IS_COUNT_ENABLED_ NUMBER(1)
		check (IS_COUNT_ENABLED_ IN (1,0)),
	EVT_SUBSCR_COUNT_ NUMBER,
	TASK_COUNT_ NUMBER,
	JOB_COUNT_ NUMBER,
	TIMER_JOB_COUNT_ NUMBER,
	SUSP_JOB_COUNT_ NUMBER,
	DEADLETTER_JOB_COUNT_ NUMBER,
	VAR_COUNT_ NUMBER,
	ID_LINK_COUNT_ NUMBER,
	CALLBACK_ID_ NVARCHAR2(255),
	CALLBACK_TYPE_ NVARCHAR2(255)
)
/

create index ACT_IDX_EXE_PROCINST
	on ACT_RU_EXECUTION (PROC_INST_ID_)
/

create index ACT_IDX_EXEC_BUSKEY
	on ACT_RU_EXECUTION (BUSINESS_KEY_)
/

create index ACT_IDX_EXE_PARENT
	on ACT_RU_EXECUTION (PARENT_ID_)
/

create index ACT_IDX_EXE_PROCDEF
	on ACT_RU_EXECUTION (PROC_DEF_ID_)
/

create index ACT_IDX_EXE_SUPER
	on ACT_RU_EXECUTION (SUPER_EXEC_)
/

create index ACT_IDX_EXEC_ROOT
	on ACT_RU_EXECUTION (ROOT_PROC_INST_ID_)
/

alter table ACT_RU_IDENTITYLINK
	add constraint ACT_FK_IDL_PROCINST
		foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_TASK
	add constraint ACT_FK_TASK_EXE
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_TASK
	add constraint ACT_FK_TASK_PROCINST
		foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_VARIABLE
	add constraint ACT_FK_VAR_EXE
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_VARIABLE
	add constraint ACT_FK_VAR_PROCINST
		foreign key (PROC_INST_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_DEADLETTER_JOB
	add constraint ACT_FK_DJOB_EXECUTION
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_DEADLETTER_JOB
	add constraint ACT_FK_DJOB_PROCESS_INSTANCE
		foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_JOB
	add constraint ACT_FK_JOB_EXECUTION
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_JOB
	add constraint ACT_FK_JOB_PROCESS_INSTANCE
		foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_SUSPENDED_JOB
	add constraint ACT_FK_SJOB_EXECUTION
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_SUSPENDED_JOB
	add constraint ACT_FK_SJOB_PROCESS_INSTANCE
		foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_TIMER_JOB
	add constraint ACT_FK_TJOB_EXECUTION
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_TIMER_JOB
	add constraint ACT_FK_TJOB_PROCESS_INSTANCE
		foreign key (PROCESS_INSTANCE_ID_) references ACT_RU_EXECUTION
/

alter table ACT_RU_EVENT_SUBSCR
	add constraint ACT_FK_EVENT_EXEC
		foreign key (EXECUTION_ID_) references ACT_RU_EXECUTION
/

create table ACT_PROCDEF_INFO
(
	ID_ NVARCHAR2(64) not null
		primary key,
	PROC_DEF_ID_ NVARCHAR2(64) not null
		constraint ACT_FK_INFO_PROCDEF
			references ACT_RE_PROCDEF,
	REV_ NUMBER,
	INFO_JSON_ID_ NVARCHAR2(64)
		constraint ACT_FK_INFO_JSON_BA
			references ACT_GE_BYTEARRAY
)
/

create index ACT_IDX_PROCDEF_INFO_PROC
	on ACT_PROCDEF_INFO (PROC_DEF_ID_)
/

alter table ACT_PROCDEF_INFO
	add constraint ACT_UNIQ_INFO_PROCDEF
		unique (PROC_DEF_ID_)
/

create index ACT_IDX_PROCDEF_INFO_JSON
	on ACT_PROCDEF_INFO (INFO_JSON_ID_)
/

create table ACT_HI_IDENTITYLINK
(
	ID_ NVARCHAR2(64) not null
		primary key,
	GROUP_ID_ NVARCHAR2(255),
	TYPE_ NVARCHAR2(255),
	USER_ID_ NVARCHAR2(255),
	TASK_ID_ NVARCHAR2(64),
	CREATE_TIME_ TIMESTAMP(6),
	PROC_INST_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255)
)
/

create index ACT_IDX_HI_IDENT_LNK_USER
	on ACT_HI_IDENTITYLINK (USER_ID_)
/

create index ACT_IDX_HI_IDENT_LNK_TASK
	on ACT_HI_IDENTITYLINK (TASK_ID_)
/

create index ACT_IDX_HI_IDENT_LNK_PROCINST
	on ACT_HI_IDENTITYLINK (PROC_INST_ID_)
/

create index ACT_IDX_HI_IDENT_LNK_SCOPE
	on ACT_HI_IDENTITYLINK (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_HI_IDENT_LNK_SCOPE_DEF
	on ACT_HI_IDENTITYLINK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create table ACT_HI_ACTINST
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER default 1,
	PROC_DEF_ID_ NVARCHAR2(64) not null,
	PROC_INST_ID_ NVARCHAR2(64) not null,
	EXECUTION_ID_ NVARCHAR2(64) not null,
	ACT_ID_ NVARCHAR2(255) not null,
	TASK_ID_ NVARCHAR2(64),
	CALL_PROC_INST_ID_ NVARCHAR2(64),
	ACT_NAME_ NVARCHAR2(255),
	ACT_TYPE_ NVARCHAR2(255) not null,
	ASSIGNEE_ NVARCHAR2(255),
	START_TIME_ TIMESTAMP(6) not null,
	END_TIME_ TIMESTAMP(6),
	DURATION_ NUMBER(19),
	DELETE_REASON_ NVARCHAR2(2000),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

create index ACT_IDX_HI_ACT_INST_PROCINST
	on ACT_HI_ACTINST (PROC_INST_ID_, ACT_ID_)
/

create index ACT_IDX_HI_ACT_INST_EXEC
	on ACT_HI_ACTINST (EXECUTION_ID_, ACT_ID_)
/

create index ACT_IDX_HI_ACT_INST_START
	on ACT_HI_ACTINST (START_TIME_)
/

create index ACT_IDX_HI_ACT_INST_END
	on ACT_HI_ACTINST (END_TIME_)
/

create table ACT_HI_PROCINST
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER default 1,
	PROC_INST_ID_ NVARCHAR2(64) not null
		unique,
	BUSINESS_KEY_ NVARCHAR2(255),
	PROC_DEF_ID_ NVARCHAR2(64) not null,
	START_TIME_ TIMESTAMP(6) not null,
	END_TIME_ TIMESTAMP(6),
	DURATION_ NUMBER(19),
	START_USER_ID_ NVARCHAR2(255),
	START_ACT_ID_ NVARCHAR2(255),
	END_ACT_ID_ NVARCHAR2(255),
	SUPER_PROCESS_INSTANCE_ID_ NVARCHAR2(64),
	DELETE_REASON_ NVARCHAR2(2000),
	TENANT_ID_ NVARCHAR2(255) default '',
	NAME_ NVARCHAR2(255),
	CALLBACK_ID_ NVARCHAR2(255),
	CALLBACK_TYPE_ NVARCHAR2(255)
)
/

create index ACT_IDX_HI_PRO_I_BUSKEY
	on ACT_HI_PROCINST (BUSINESS_KEY_)
/

create index ACT_IDX_HI_PRO_INST_END
	on ACT_HI_PROCINST (END_TIME_)
/

create table ACT_HI_TASKINST
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER default 1,
	PROC_DEF_ID_ NVARCHAR2(64),
	TASK_DEF_ID_ NVARCHAR2(64),
	TASK_DEF_KEY_ NVARCHAR2(255),
	PROC_INST_ID_ NVARCHAR2(64),
	EXECUTION_ID_ NVARCHAR2(64),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	SCOPE_DEFINITION_ID_ NVARCHAR2(255),
	PARENT_TASK_ID_ NVARCHAR2(64),
	NAME_ NVARCHAR2(255),
	DESCRIPTION_ NVARCHAR2(2000),
	OWNER_ NVARCHAR2(255),
	ASSIGNEE_ NVARCHAR2(255),
	START_TIME_ TIMESTAMP(6) not null,
	CLAIM_TIME_ TIMESTAMP(6),
	END_TIME_ TIMESTAMP(6),
	DURATION_ NUMBER(19),
	DELETE_REASON_ NVARCHAR2(2000),
	PRIORITY_ NUMBER,
	DUE_DATE_ TIMESTAMP(6),
	FORM_KEY_ NVARCHAR2(255),
	CATEGORY_ NVARCHAR2(255),
	TENANT_ID_ NVARCHAR2(255) default '',
	LAST_UPDATED_TIME_ TIMESTAMP(6)
)
/

create index ACT_IDX_HI_TASK_INST_PROCINST
	on ACT_HI_TASKINST (PROC_INST_ID_)
/

create index ACT_IDX_HI_TASK_SCOPE
	on ACT_HI_TASKINST (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_HI_TASK_SUB_SCOPE
	on ACT_HI_TASKINST (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_HI_TASK_SCOPE_DEF
	on ACT_HI_TASKINST (SCOPE_DEFINITION_ID_, SCOPE_TYPE_)
/

create table ACT_HI_VARINST
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER default 1,
	PROC_INST_ID_ NVARCHAR2(64),
	EXECUTION_ID_ NVARCHAR2(64),
	TASK_ID_ NVARCHAR2(64),
	NAME_ NVARCHAR2(255) not null,
	VAR_TYPE_ NVARCHAR2(100),
	SCOPE_ID_ NVARCHAR2(255),
	SUB_SCOPE_ID_ NVARCHAR2(255),
	SCOPE_TYPE_ NVARCHAR2(255),
	BYTEARRAY_ID_ NVARCHAR2(64),
	DOUBLE_ NUMBER(*,10),
	LONG_ NUMBER(19),
	TEXT_ NVARCHAR2(2000),
	TEXT2_ NVARCHAR2(2000),
	CREATE_TIME_ TIMESTAMP(6),
	LAST_UPDATED_TIME_ TIMESTAMP(6)
)
/

create index ACT_IDX_HI_PROCVAR_PROC_INST
	on ACT_HI_VARINST (PROC_INST_ID_)
/

create index ACT_IDX_HI_PROCVAR_EXE
	on ACT_HI_VARINST (EXECUTION_ID_)
/

create index ACT_IDX_HI_PROCVAR_TASK_ID
	on ACT_HI_VARINST (TASK_ID_)
/

create index ACT_IDX_HI_PROCVAR_NAME_TYPE
	on ACT_HI_VARINST (NAME_, VAR_TYPE_)
/

create index ACT_IDX_HI_VAR_SCOPE_ID_TYPE
	on ACT_HI_VARINST (SCOPE_ID_, SCOPE_TYPE_)
/

create index ACT_IDX_HI_VAR_SUB_ID_TYPE
	on ACT_HI_VARINST (SUB_SCOPE_ID_, SCOPE_TYPE_)
/

create table ACT_HI_ATTACHMENT
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	USER_ID_ NVARCHAR2(255),
	NAME_ NVARCHAR2(255),
	DESCRIPTION_ NVARCHAR2(2000),
	TYPE_ NVARCHAR2(255),
	TASK_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	URL_ NVARCHAR2(2000),
	CONTENT_ID_ NVARCHAR2(64),
	TIME_ TIMESTAMP(6)
)
/

create table ACT_HI_COMMENT
(
	ID_ NVARCHAR2(64) not null
		primary key,
	TYPE_ NVARCHAR2(255),
	TIME_ TIMESTAMP(6) not null,
	USER_ID_ NVARCHAR2(255),
	TASK_ID_ NVARCHAR2(64),
	PROC_INST_ID_ NVARCHAR2(64),
	ACTION_ NVARCHAR2(255),
	MESSAGE_ NVARCHAR2(2000),
	FULL_MSG_ BLOB
)
/

create table ACT_HI_DETAIL
(
	ID_ NVARCHAR2(64) not null
		primary key,
	TYPE_ NVARCHAR2(255) not null,
	PROC_INST_ID_ NVARCHAR2(64),
	EXECUTION_ID_ NVARCHAR2(64),
	TASK_ID_ NVARCHAR2(64),
	ACT_INST_ID_ NVARCHAR2(64),
	NAME_ NVARCHAR2(255) not null,
	VAR_TYPE_ NVARCHAR2(64),
	REV_ NUMBER,
	TIME_ TIMESTAMP(6) not null,
	BYTEARRAY_ID_ NVARCHAR2(64),
	DOUBLE_ NUMBER(*,10),
	LONG_ NUMBER(19),
	TEXT_ NVARCHAR2(2000),
	TEXT2_ NVARCHAR2(2000)
)
/

create index ACT_IDX_HI_DETAIL_PROC_INST
	on ACT_HI_DETAIL (PROC_INST_ID_)
/

create index ACT_IDX_HI_DETAIL_TASK_ID
	on ACT_HI_DETAIL (TASK_ID_)
/

create index ACT_IDX_HI_DETAIL_ACT_INST
	on ACT_HI_DETAIL (ACT_INST_ID_)
/

create index ACT_IDX_HI_DETAIL_NAME
	on ACT_HI_DETAIL (NAME_)
/

create index ACT_IDX_HI_DETAIL_TIME
	on ACT_HI_DETAIL (TIME_)
/

create table ACT_ID_GROUP
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	NAME_ NVARCHAR2(255),
	TYPE_ NVARCHAR2(255)
)
/

create table ACT_ID_MEMBERSHIP
(
	USER_ID_ NVARCHAR2(64) not null,
	GROUP_ID_ NVARCHAR2(64) not null
		constraint ACT_FK_MEMB_GROUP
			references ACT_ID_GROUP,
	primary key (USER_ID_, GROUP_ID_)
)
/

create index ACT_IDX_MEMB_USER
	on ACT_ID_MEMBERSHIP (USER_ID_)
/

create index ACT_IDX_MEMB_GROUP
	on ACT_ID_MEMBERSHIP (GROUP_ID_)
/

create table ACT_ID_PROPERTY
(
	NAME_ NVARCHAR2(64) not null
		primary key,
	VALUE_ NVARCHAR2(300),
	REV_ NUMBER
)
/

create table ACT_ID_USER
(
	ID_ NVARCHAR2(64) not null
		primary key,
	REV_ NUMBER,
	FIRST_ NVARCHAR2(255),
	LAST_ NVARCHAR2(255),
	EMAIL_ NVARCHAR2(255),
	PWD_ NVARCHAR2(255),
	PICTURE_ID_ NVARCHAR2(64),
	TENANT_ID_ NVARCHAR2(255) default ''
)
/

alter table ACT_ID_MEMBERSHIP
	add constraint ACT_FK_MEMB_USER
		foreign key (USER_ID_) references ACT_ID_USER
/

create table ACT_DE_MODEL_RELATION
(
	ID NVARCHAR2(255) not null
		primary key,
	PARENT_MODEL_ID NVARCHAR2(255) default NULL,
	MODEL_ID NVARCHAR2(255) default NULL,
	RELATION_TYPE NVARCHAR2(255) default NULL
)
/

create table ACT_DE_MODEL
(
	ID NVARCHAR2(255) not null
		primary key,
	NAME NVARCHAR2(400) not null,
	MODEL_KEY NVARCHAR2(400) not null,
	DESCRIPTION NVARCHAR2(400) default NULL,
	MODEL_COMMENT NVARCHAR2(400) default NULL,
	CREATED DATE default NULL,
	CREATED_BY NVARCHAR2(255) default NULL,
	LAST_UPDATED DATE default NULL,
	LAST_UPDATED_BY NVARCHAR2(255) default NULL,
	VERSION NUMBER(11) default NULL,
	MODEL_EDITOR_JSON CLOB,
	THUMBNAIL BLOB,
	MODEL_TYPE NUMBER(11) default NULL,
	DEPLOY_ID NVARCHAR2(255) default NULL,
	PROC_DEF_ID NVARCHAR2(255) default NULL,
	PROC_DEF_KEY NVARCHAR2(255) default NULL
)
/

create table ORDER_INST
(
	ID NVARCHAR2(50),
	BUSINESS_KEY NVARCHAR2(50),
	PROC_INST_ID NVARCHAR2(50),
	ORDER_NAME NVARCHAR2(100),
	ORDER_CODE NVARCHAR2(50),
	CREATER NVARCHAR2(20),
	CREATED_BY NVARCHAR2(20),
	CREATE_TIME DATE,
	STATUS NUMBER(1)
)
/

create table ACT_ID_PRIV
(
	ID_ NVARCHAR2(64) not null
		primary key,
	NAME_ NVARCHAR2(255)
)
/

create table ACT_ID_PRIV_MAPPING
(
	ID_ NVARCHAR2(64) not null
		primary key,
	PRIV_ID_ NVARCHAR2(64) not null
		constraint ACT_FK_PRIV_MAPPING
			references ACT_ID_PRIV,
	USER_ID_ NVARCHAR2(255),
	GROUP_ID_ NVARCHAR2(255)
)
/

create index ACT_IDX_PRIV_MAPPING
	on ACT_ID_PRIV_MAPPING (PRIV_ID_)
/

create index ACT_IDX_PRIV_USER
	on ACT_ID_PRIV_MAPPING (USER_ID_)
/

create index ACT_IDX_PRIV_GROUP
	on ACT_ID_PRIV_MAPPING (GROUP_ID_)
/

INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('cfg.execution-related-entities-count', 'true', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('cfg.task-related-entities-count', 'true', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('common.schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('next.dbid', '1', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('identitylink.schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('task.schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('variable.schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('job.schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('schema.version', '6.3.1.0', 1);
INSERT INTO OCS.ACT_GE_PROPERTY (NAME_, VALUE_, REV_) VALUES ('schema.history', 'create(6.3.1.0)', 1);

INSERT INTO OCS.ACT_ID_PROPERTY (NAME_, VALUE_, REV_) VALUES ('schema.version', '6.3.1.0', 1);

