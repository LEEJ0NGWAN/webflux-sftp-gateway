CREATE TABLE `ssh_key` (
  `id` varchar(50) NOT NULL,
  `private_key` text,
  `public_key` text,
  PRIMARY KEY (`id`)
);

INSERT INTO `ssh_key` VALUES ('sample_key_id',STRINGDECODE('SAMPLE_PRIVATE_KEY_STRING'),'SAMPLE_PUBLIC_KEY_STRING');
