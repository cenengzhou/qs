alter table pcmsdataprod.job_info
add (
                EOT_APPLIED NUMBER(10),
                EOT_AWARDED NUMBER(10),
                LD_EXPOSURE_AMOUNT NUMBER(19,2),
                GD_EXPOSURE_AMOUNT NUMBER(19,2)
);

alter table pcmsdataprod.job_info_audit
add (
                EOT_APPLIED NUMBER(10),
                EOT_AWARDED NUMBER(10),
                LD_EXPOSURE_AMOUNT NUMBER(19,2),
                GD_EXPOSURE_AMOUNT NUMBER(19,2)
);
