package com.gammon.pcms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.gammon.pcms.config.AttachmentConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.Drive;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.DriveItemCreateUploadSessionParameterSet;
import com.microsoft.graph.models.DriveItemUploadableProperties;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.Site;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.graph.models.UserSendMailParameterSet;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import com.microsoft.graph.requests.DriveItemCollectionRequestBuilder;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.tasks.IProgressCallback;
import com.microsoft.graph.tasks.LargeFileUploadResult;
import com.microsoft.graph.tasks.LargeFileUploadTask;

@Service
public class Unc2SharePointService {
  @Autowired
  private AttachmentConfig attachmentConfig;
  @Autowired
  private WebServiceConfig webServiceConfig;

  public String CheckAndCopyToSharePoint(boolean sendAlertEmail) {
    return CheckAndCopyToSharePoint(null, sendAlertEmail);
  }

  public String CheckAndCopyToSharePoint(Map<String, Map<String, String[]>> jobsFilterMap, boolean sendAlertEmail) {
    return Unc2SharePoint.newInstance(attachmentConfig, webServiceConfig, jobsFilterMap, sendAlertEmail);
  }

  public String CheckAndCopyToSharePoint(Object jobNo, Object subcontractNo, Object paymentCertNo, boolean sendAlertEmail) {
    return CheckAndCopyToSharePoint(jobNo != null ? "" + jobNo : null,
        subcontractNo != null ? "" + subcontractNo : null, paymentCertNo != null ? "" + paymentCertNo : null, sendAlertEmail);
  }

  public String CheckAndCopyToSharePoint(String jobNo, String subcontractNo, String paymentCertNo, boolean sendAlertEmail) {
    return CheckAndCopyToSharePoint(
        StringUtils.isNotBlank(jobNo) ? Arrays.asList(jobNo) : null,
        StringUtils.isNotBlank(subcontractNo) ? Arrays.asList(subcontractNo) : null,
        StringUtils.isNotBlank(paymentCertNo) ? Arrays.asList(paymentCertNo) : null,
        sendAlertEmail);
  }

  public String CheckAndCopyToSharePoint(List<String> jobNoList, List<String> subcontractNoList,
      List<String> paymentCertNoList, boolean sendAlertEmail) {

    Map<String, Map<String, String[]>> jobsFilterMap = null;
    if (jobNoList != null && jobNoList.size() > 0) {
      jobsFilterMap = new HashMap<String, Map<String, String[]>>() {
        {
          jobNoList.forEach(jobNo -> {
            put(jobNo, new HashMap<String, String[]>() {
              {
                if (subcontractNoList != null && subcontractNoList.size() > 0) {
                  subcontractNoList.forEach(subcontractNo -> {
                    put(subcontractNo,
                        paymentCertNoList != null ? paymentCertNoList.stream().toArray(String[]::new) : null);
                  });
                }
              }
            });
          });
        }
      };
    }
    return Unc2SharePoint.newInstance(attachmentConfig, webServiceConfig, jobsFilterMap, sendAlertEmail);
  }

  public static class Unc2SharePoint {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private GraphServiceClient graphServiceClient;
    private StringBuilder statusStringBuilder = new StringBuilder();

    private String uncServerPath;
    private String uncRootPath;

    private String sharePointSitePath;
    private String sharePointRootPath;
    private String sharePointCertPath;

    private List<Unc2SharePointFolder> folderList = new ArrayList<>();

    private Map<String, String[]> filterMap;
    private List<String> notifyList;
    private List<String> notifyCcList;
    private String sender;
    private boolean sendAlertEmail;
    
    private Unc2SharePoint() {
    }

    public static String newInstance(AttachmentConfig attachmentConfig, WebServiceConfig webServiceConfig, boolean sendAlertEmail) {
      return newInstance(attachmentConfig, webServiceConfig, null, sendAlertEmail);
    }

    public static String newInstance(AttachmentConfig attachmentConfig, WebServiceConfig webServiceConfig,
        Map<String, Map<String, String[]>> jobsFilterMap, boolean sendAlertEmail) {
      String uncServerPath = attachmentConfig.getAttachmentServer("PATH")
          + attachmentConfig.getJobAttachmentsDirectory();
      Map<String, Map<String, String>> scpaymentMergeJobMap = attachmentConfig.getScpaymentMergeJobMap();
      Set<String> jobs = jobsFilterMap != null ? jobsFilterMap.keySet() : scpaymentMergeJobMap.keySet();
      StringBuilder output = new StringBuilder();
      jobs.stream().forEach(job -> {
        Map<String, String> config = scpaymentMergeJobMap.get(job);
        if (config != null) {
          Map<String, String[]> filterMap = jobsFilterMap != null ? jobsFilterMap.get(job) : null;
          String sharePointSitePath = webServiceConfig.getWsAzureTenantDomainPrefix() + ".sharepoint.com:/sites/"
              + config.get("sharePointSitePath");
          String sharePointRootPath = config.get("sharePointRootPath");
          String sharePointCertPath = config.get("sharePointCertPath");
          List<String> notifyList = splitString(attachmentConfig.getScpaymentMergeAlertMap().get(job));
          List<String> notifyCcList = splitString(attachmentConfig.getScpaymentMergeAlertAdmin());
          String sender = webServiceConfig.getWsAzureAlertSender();
          Unc2SharePoint unc2SharePoint = new Unc2SharePoint()
              .setUncServerPath(uncServerPath)
              .setUncRootPath(job + "\\Subcontract")
              .setSharePointSitePath(sharePointSitePath)
              .setSharePointRootPath(sharePointRootPath)
              .setSharePointCertPath(sharePointCertPath)
              .setGraphServiceClient(getGraphServiceClient(webServiceConfig))
              .setFilterMap(filterMap)
              .setNotifyList(notifyList)
              .setNotifyCcList(notifyCcList)
              .setSender(sender)
              .setSendAlertEmail(sendAlertEmail)
              ;
          output.append(unc2SharePoint.CheckAndCopy());
        } else {
          output.append("\n" + job + " config not defined");
        }
      });
      return output.toString();
    }

    private String CheckAndCopy() {
      String result = this.LoadUnc().LoadSharePoint().UploadUnc2SharePoint().toString();
      StringBuffer output = new StringBuffer();
      output.append("\n\n" + StringUtils.repeat("::", 80));
      if (statusStringBuilder.length() > 0) {
        output.append("\n\n");
        output.append("\n" + statusStringBuilder.toString() + "\n");
        output.append("\n\n");
      }
      output.append(result);
      output.append("\n" + StringUtils.repeat("::", 80) + "\n\n");
      if (statusStringBuilder.length() > 0 && isSendAlertEmail()) {
        List<String> notifyList = getNotifyList();
        List<String> notifyCcList = getNotifyCcList();
        if (notifyList != null && notifyList.size() > 0) {
          String subject = "Payment Certificate merge status (" + getUncRootPath().split("\\\\")[0] + ")";
          mergeAlert(notifyList, notifyCcList, subject, output.toString());
        }
      }
      logger.info(output.toString());
      return output.toString();
    }

    private Unc2SharePoint LoadUnc() {
      if (StringUtils.isBlank(getUncRootPath())) {
        throw new IllegalArgumentException("uncRootPath empty");
      }
      File rootUncFolder = new File(getUncFullPath());
      String[] folderPaths = getFilteredFolderPath(rootUncFolder.list(getFileTypeFilter("folder")));
      LoadUncSubPath(rootUncFolder, folderPaths);
      return this;
    }

    private Unc2SharePoint LoadUncSubPath(File rootUncFolder, String[] folderPaths) {
      Arrays.asList(folderPaths).forEach(folderPath -> {
        File uncFolder = new File(rootUncFolder, folderPath);
        Unc2SharePointFolder folder = new Unc2SharePointFolder();
        folder.setUncPath(folderPath);
        String[] filePaths = getFilteredFilePath(folderPath, uncFolder.list(getFileTypeFilter("file")));
        LoadUncFile(folder, filePaths);
      });
      return this;
    }

    private Unc2SharePoint LoadUncFile(Unc2SharePointFolder folder, String[] filePaths) {
      Arrays.asList(filePaths).forEach(filePath -> {
        Unc2SharePointFile file = new Unc2SharePointFile();
        file.setUncPath(filePath);
        folder.getFileList().add(file);
      });
      this.getFolderList().add(folder);
      return this;
    }

    private Unc2SharePoint LoadSharePoint() {
      if (graphServiceClient == null || sharePointSitePath == null || sharePointRootPath == null) {
        throw new IllegalArgumentException(
            "sharePointGraphServiceClient=" + graphServiceClient + " sharePointSitePath=" + sharePointSitePath
                + " sharePointRootPath=" + sharePointRootPath);
      }
      DriveItem sharePointRootFolder = getGraphServiceClient().sites(getSite().id).drive().root()
          .itemWithPath(getSharePointRootPath())
          .buildRequest().get();
      List<DriveItem> sharePointSubFolderList = getFilteredFolderList(
          getDriveFolderList(getDrive().id, sharePointRootFolder.id));

      LoadSharePointSubFolder(sharePointSubFolderList);
      return this;
    }

    private Unc2SharePoint LoadSharePointSubFolder(List<DriveItem> sharePointSubFolderList) {
      sharePointSubFolderList.stream().forEach(sharePointFolder -> {
        Unc2SharePointFolder unc2SharePointFolder = this.getFolderList().stream()
            .filter(folder -> sharePointFolder.name.contains(folder.uncPath))
            .findFirst()
            .orElse(null);

        if (unc2SharePointFolder != null) {
          unc2SharePointFolder.setSharePointFolder(sharePointFolder);
          LoadSharePointCertFolder(unc2SharePointFolder);
        } else {
          logger
              .warn("\n[::sharePointFolder !@ unc::] " + getSharePointSitePath() + "/" + getSharePointRootPath() + "/"
                  + sharePointFolder.name + "\n");
        }
      });
      return this;
    }

    private Unc2SharePoint LoadSharePointCertFolder(Unc2SharePointFolder unc2SharePointFolder) {
      DriveItem sharePointFolder = unc2SharePointFolder.getSharePointFolder();
      if (sharePointFolder == null) {
        throw new ClientException("SharePoint folder not found", null);
      }

      boolean haveCertConfig = StringUtils.isNotBlank(getSharePointCertPath());
      DriveItem sharePointCertFolder = haveCertConfig
          ? getDriveFolder(getDrive().id, sharePointFolder.id, getSharePointCertPath())
          : null;
      if (sharePointCertFolder != null) {
        unc2SharePointFolder.setSharePointCertFolder(sharePointCertFolder);
      } else {
        if (haveCertConfig) {
          statusStringBuilder.append("\n[::sharePointCertFolder !@ SharePoint::] " + getSharePointSitePath() + "/"
              + getSharePointRootPath() + "/"
              + sharePointFolder.name + "/*" + getSharePointCertPath() + "*");
        }
      }

      String certFolderId = sharePointCertFolder != null ? sharePointCertFolder.id : sharePointFolder.id;
      List<DriveItem> sharePointFileList = getFilteredFileList(unc2SharePointFolder.getUncPath(),
          getDriveFileList(getDrive().id, certFolderId));
      List<Unc2SharePointFile> unc2SharePointFileList = unc2SharePointFolder.getFileList();

      LoadSharePointFolderItem(sharePointFileList, unc2SharePointFileList,
          sharePointFolder.name + (sharePointCertFolder != null ? "/" + sharePointCertFolder.name : ""));
      return this;
    }

    private Unc2SharePoint LoadSharePointFolderItem(List<DriveItem> sharePointFileList,
        List<Unc2SharePointFile> unc2SharePointFileList, String sharePointPath) {
      sharePointFileList.stream().forEach(sharePointFile -> {
        Unc2SharePointFile unc2SharePointFile = unc2SharePointFileList.stream()
            .filter(file -> file.uncPath.equals(sharePointFile.name)).findFirst()
            .orElse(null);

        if (unc2SharePointFile != null) {
          unc2SharePointFile.setSharePointFile(sharePointFile);
        } else {
          logger
              .warn("\n[::sharePointFile !@ unc::] " + getSharePointSitePath() + "/" + getSharePointRootPath() + "/"
                  + sharePointPath + "/" + sharePointFile.name + "\n");
        }
      });
      return this;
    }

    private Unc2SharePoint UploadUnc2SharePoint() {
      this.getFolderList().stream().forEach(unc2SharePointFolder -> {
        UploadUnc2SharePointFolder(unc2SharePointFolder);
      });
      return this;
    }

    private Unc2SharePoint UploadUnc2SharePointFolder(Unc2SharePointFolder unc2SharePointFolder) {
      List<Unc2SharePointFile> unc2SharePointFileList = unc2SharePointFolder.getFileList();
      unc2SharePointFileList.stream().forEach(unc2SharePointFile -> {
        if (unc2SharePointFile.getSharePointFile() == null) {
          String itemId = unc2SharePointFolder.getSharePointCertFolder() != null
              ? unc2SharePointFolder.getSharePointCertFolder().id
              : StringUtils.isBlank(getSharePointCertPath()) && unc2SharePointFolder.getSharePointFolder() != null
                  ? unc2SharePointFolder.getSharePointFolder().id
                  : null;
          if (StringUtils.isNotBlank(itemId)) {
            UploadUnc2SharePointFile(unc2SharePointFolder, unc2SharePointFile);
          } else {
            statusStringBuilder.append(
                "\n" + getSharePointSitePath() + "/" + getSharePointRootPath() + "/" + unc2SharePointFolder.getUncPath()
                    + (getSharePointCertPath() != null ? "/*" + getSharePointCertPath() + "*" : "") + " not found for "
                    + unc2SharePointFile.getUncPath());
          }
        }
      });
      return this;

    }

    private void UploadUnc2SharePointFile(Unc2SharePointFolder unc2SharePointFolder,
        Unc2SharePointFile unc2SharePointFile) {
      File uncFile = new File(getUncFullPath() + "\\" + unc2SharePointFolder.getUncPath(),
          unc2SharePointFile.getUncPath());
      try (InputStream fileStream = new FileInputStream(uncFile)) {
        long streamSize = uncFile.length();
        UploadSession uploadSession = getUploadSession(unc2SharePointFolder, uncFile);

        LargeFileUploadTask<DriveItem> largeFileUploadTask = new LargeFileUploadTask<DriveItem>(uploadSession,
            getGraphServiceClient(), fileStream, streamSize, DriveItem.class);
        LargeFileUploadResult<DriveItem> uploadedItem = largeFileUploadTask.upload(0, null, UploadItemCallBack());

        unc2SharePointFile.setSharePointFile(uploadedItem.responseBody);
        unc2SharePointFile.setNewUpload(true);
        logger.info("\nfile uploaded: " + uploadedItem.responseBody.webUrl);
      } catch (ClientException | IOException e) {
        e.printStackTrace();
        statusStringBuilder.append("\ncannot upload " + unc2SharePointFile.uncPath);
      }
    }

    public IProgressCallback UploadItemCallBack() {
      IProgressCallback callback = new IProgressCallback() {
        @Override
        public void progress(final long current, final long max) {
          System.out.println(
              String.format("Uploaded %d bytes of %d total bytes", current, max));
        }
      };
      return callback;
    }

    private UploadSession getUploadSession(Unc2SharePointFolder unc2SharePointFolder, File uncFile) {
      DriveItemCreateUploadSessionParameterSet uploadParams = DriveItemCreateUploadSessionParameterSet
          .newBuilder()
          .withItem(new DriveItemUploadableProperties()).build();

      UploadSession uploadSession = getGraphServiceClient()
          .drives(getDrive().id)
          .items(
              unc2SharePointFolder.getSharePointCertFolder() != null ? unc2SharePointFolder.getSharePointCertFolder().id
                  : unc2SharePointFolder.getSharePointFolder().id)
          .itemWithPath(uncFile.getName())
          .createUploadSession(uploadParams)
          .buildRequest()
          .post();
      return uploadSession;
    }

    private static GraphServiceClient getGraphServiceClient(WebServiceConfig webServiceConfig) {
      ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
          .clientId(webServiceConfig.getWsAzureClientId())
          .clientSecret(webServiceConfig.getWsAzureClientSecret())
          .tenantId(webServiceConfig.getWsAzureTenantId())
          .build();

      TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(
          Arrays.asList("https://graph.microsoft.com/.default"),
          clientSecretCredential);

      GraphServiceClient graphClient = GraphServiceClient
          .builder()
          .authenticationProvider(tokenCredentialAuthProvider)
          .buildClient();
      return graphClient;
    }
    
    private void mergeAlert(List<String> emailList, List<String> emailCcList, String subject, String bodyContent) {
      Message message = new Message();
      message.subject = subject;
      ItemBody body = new ItemBody();
      body.contentType = BodyType.TEXT;
      body.content = bodyContent;
      message.body = body;

      message.toRecipients = createRecipientList(emailList);
      message.ccRecipients = createRecipientList(emailCcList);

      getGraphServiceClient()
          .users(getSender())
          .sendMail(
              UserSendMailParameterSet
                  .newBuilder()
                  .withMessage(message)
                  .withSaveToSentItems(true)
                  .build())
          .buildRequest().post();
    }

    private LinkedList<Recipient> createRecipientList(List<String> emailList) {
      if (emailList != null && emailList.size() > 0) {
        LinkedList<Recipient> recipientList = new LinkedList<Recipient>();
        emailList.forEach(email -> {
          Recipient recipient = new Recipient();
          EmailAddress emailAddress = new EmailAddress();
          emailAddress.address = email;
          recipient.emailAddress = emailAddress;
          recipientList.add(recipient);
        });
        return recipientList;
      }
      return null;
    }

    private Site getSite() {
      return getGraphServiceClient().sites(getSharePointSitePath()).buildRequest().get();
    }

    private Drive getDrive() {
      return getGraphServiceClient().sites(getSite().id).drive().buildRequest().get();
    }

    private List<DriveItem> getDriveFileList(String driveId, String itemId) {
      return getAllDriveItem(driveId, itemId).stream().filter(i -> i.file != null).collect(Collectors.toList());
    }

    private DriveItem getDriveFolder(String driveId, String itemId, String name) {
      return getDriveFolderList(driveId, itemId).stream().filter(p -> p.folder != null && p.name.contains(name))
          .findFirst().orElse(null);
    }

    private List<DriveItem> getDriveFolderList(String driveId, String itemId) {
      return getAllDriveItem(driveId, itemId).stream().filter(i -> i.folder != null).collect(Collectors.toList());
    }

    private List<DriveItem> getAllDriveItem(String driveId, String itemId) {
      DriveItemCollectionPage sharePointRootItems = getGraphServiceClient().drives(driveId).items(itemId).children()
          .buildRequest().get();
      List<DriveItem> driveItemList = getAllDriveItem(sharePointRootItems);
      return driveItemList;
    }

    private List<DriveItem> getAllDriveItem(DriveItemCollectionPage page) {
      List<DriveItem> driveItemList = page.getCurrentPage();
      DriveItemCollectionRequestBuilder nextPageBuilder = page.getNextPage();
      if (nextPageBuilder != null) {
        DriveItemCollectionPage nextPage = nextPageBuilder.buildRequest().get();
        driveItemList.addAll(getAllDriveItem(nextPage));
      }
      return driveItemList;
    }

    private FilenameFilter getFileTypeFilter(String fileType) {
      boolean isDirectory = "folder".equals(fileType.toLowerCase());
      return new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return isDirectory == new File(dir, name).isDirectory();
        }
      };
    }

    private String[] getFilteredFolderPath(String[] folderPaths) {
      if (getFilterMap() != null) {
        Set<String> folders = getFilterMap().keySet();
        if (folders != null && folders.size() > 0) {
          folderPaths = Arrays.asList(folderPaths).stream().filter(f -> folders.contains(f)).toArray(String[]::new);
        }
      }
      return folderPaths;
    }

    private List<DriveItem> getFilteredFolderList(List<DriveItem> folderList) {
      if (getFilterMap() != null) {
        Set<String> folders = getFilterMap().keySet();
        if (folders != null && folders.size() > 0) {
          folderList = folderList.stream().filter(f -> folders.contains(f.name)).collect(Collectors.toList());
        }
      }
      return folderList;
    }

    private List<DriveItem> getFilteredFileList(String folder, List<DriveItem> fileList) {
      if (getFilterMap() != null) {
        String[] files = getFilterMap().get(folder);
        if (files != null && files.length > 0) {
          fileList = fileList.stream()
              .filter(f -> Arrays.asList(files).contains(getCertNo(f.name)))
              .collect(Collectors.toList());
        }
      }
      return fileList;
    }

    private String[] getFilteredFilePath(String folder, String[] filePaths) {
      if (getFilterMap() != null) {
        String[] files = getFilterMap().get(folder);
        if (files != null && files.length > 0) {
          filePaths = Arrays.asList(filePaths).stream()
              .filter(f -> Arrays.asList(files).contains(getCertNo(f)))
              .toArray(String[]::new);
        }
      }
      return filePaths;
    }

    private String getCertNo(String filename) {
      return StringUtils.stripStart(filename.replaceAll("\\d+-\\d+-(\\d+).\\w+", "$1"), "0");
    }

    private static List<String> splitString(String string) {
      List<String> stringList = null;
      if (StringUtils.isNotBlank(string)) {
        stringList = Arrays.stream(string.split(","))
            .map(String::trim).collect(Collectors.toList());
      }
      return stringList;
    }

    public String getUncServerPath() {
      return uncServerPath;
    }

    public Unc2SharePoint setUncServerPath(String uncServerPath) {
      this.uncServerPath = uncServerPath;
      return this;
    }

    public String getUncFullPath() {
      return getUncServerPath() + "\\" + uncRootPath;
    }

    public String getUncRootPath() {
      return this.uncRootPath;
    }

    public Unc2SharePoint setUncRootPath(String uncRootPath) {
      this.uncRootPath = uncRootPath;
      return this;
    }

    public GraphServiceClient getGraphServiceClient() {
      return graphServiceClient;
    }

    public Unc2SharePoint setGraphServiceClient(GraphServiceClient graphServiceClient) {
      this.graphServiceClient = graphServiceClient;
      return this;
    }

    public String getSharePointSitePath() {
      return this.sharePointSitePath;
    }

    public Unc2SharePoint setSharePointSitePath(String sharePointSitePath) {
      this.sharePointSitePath = sharePointSitePath;
      return this;
    }

    public String getSharePointRootPath() {
      return this.sharePointRootPath;
    }

    public Unc2SharePoint setSharePointRootPath(String sharePointRootPath) {
      this.sharePointRootPath = sharePointRootPath;
      return this;
    }

    public String getSharePointCertPath() {
      return this.sharePointCertPath;
    }

    public Unc2SharePoint setSharePointCertPath(String sharePointCertPath) {
      this.sharePointCertPath = sharePointCertPath;
      return this;
    }

    public List<Unc2SharePointFolder> getFolderList() {
      return this.folderList;
    }

    public Unc2SharePoint setFolderList(List<Unc2SharePointFolder> folderList) {
      this.folderList = folderList;
      return this;
    }

    public Map<String, String[]> getFilterMap() {
      return filterMap;
    }

    public Unc2SharePoint setFilterMap(Map<String, String[]> filterMap) {
      this.filterMap = filterMap;
      return this;
    }

    public List<String> getNotifyList() {
      return notifyList;
    }

    public Unc2SharePoint setNotifyList(List<String> notifyList) {
      this.notifyList = notifyList;
      return this;
    }

    public List<String> getNotifyCcList() {
      return notifyCcList;
    }

    public Unc2SharePoint setNotifyCcList(List<String> notifyCcList) {
      this.notifyCcList = notifyCcList;
      return this;
    }

    public String getSender() {
      return sender;
    }

    public Unc2SharePoint setSender(String sender) {
      this.sender = sender;
      return this;
    }

    public boolean isSendAlertEmail() {
      return sendAlertEmail;
    }

    public Unc2SharePoint setSendAlertEmail(boolean sendAlertEmail) {
      this.sendAlertEmail = sendAlertEmail;
      return this;
    }


    public String toString() {
      return "\n{ uncRootPath:" + uncRootPath + ", sharePointSitePath:" + sharePointSitePath
          + ", sharePointRootPath:" + sharePointRootPath
          + (sharePointCertPath != null ? ", sharePointCertPath: *" + sharePointCertPath + "*" : "") + " }\n"
          + "\t" + folderList.stream().map(Unc2SharePointFolder::toString).collect(Collectors.toList()) + "\n";
    }

    public class Unc2SharePointFile {
      private String uncPath;
      private DriveItem sharePointFile;
      private boolean newUpload;

      public String getUncPath() {
        return this.uncPath;
      }

      public Unc2SharePointFile setUncPath(String uncPath) {
        this.uncPath = uncPath;
        return this;
      }

      public DriveItem getSharePointFile() {
        return this.sharePointFile;
      }

      public Unc2SharePointFile setSharePointFile(DriveItem sharePointFile) {
        this.sharePointFile = sharePointFile;
        return this;
      }

      public boolean isNewUpload() {
        return newUpload;
      }

      public void setNewUpload(boolean newUpload) {
        this.newUpload = newUpload;
      }

      @Override
      public String toString() {
        return "\n\t\t{ uncFile:" + uncPath + ", sharePointFile:"
            + (sharePointFile != null ? sharePointFile.name + " (" + sharePointFile.id + ")" : "not found")
            + (sharePointFile != null && newUpload ? "*" : "")
            + " }";
      }
    }

    public class Unc2SharePointFolder {
      private String uncPath; // 1101
      private DriveItem sharePointFolder; // 1101 || 2108 (1101)
      private DriveItem sharePointCertFolder; // Certificate || 2) Certificate

      private List<Unc2SharePointFile> fileList = new ArrayList<>();

      public String getUncPath() {
        return this.uncPath;
      }

      public Unc2SharePointFolder setUncPath(String uncPath) {
        this.uncPath = uncPath;
        return this;
      }

      public DriveItem getSharePointFolder() {
        return this.sharePointFolder;
      }

      public Unc2SharePointFolder setSharePointFolder(DriveItem sharePointFolder) {
        this.sharePointFolder = sharePointFolder;
        return this;
      }

      public DriveItem getSharePointCertFolder() {
        return sharePointCertFolder;
      }

      public void setSharePointCertFolder(DriveItem sharePointCertFolder) {
        this.sharePointCertFolder = sharePointCertFolder;
      }

      public List<Unc2SharePointFile> getFileList() {
        return this.fileList;
      }

      public Unc2SharePointFolder setFileList(List<Unc2SharePointFile> fileList) {
        this.fileList = fileList;
        return this;
      }

      @Override
      public String toString() {
        return "\n\n\t{ uncFolder:" + uncPath
            + ", sharePointFolder:"
            + (sharePointFolder != null ? sharePointFolder.name + " (" + sharePointFolder.id + ")" : null)
            + (StringUtils.isNotBlank(getSharePointCertPath()) ? ", sharePointCertFolder:"
                + (sharePointCertFolder != null ? sharePointCertFolder.name + " (" + sharePointCertFolder.id + ")"
                    : null)
                : "")
            + " }\n"
            + "\t\t" + fileList.stream().map(Unc2SharePointFile::toString).collect(Collectors.toList()) + "\n";
      }

    }

  }

}
