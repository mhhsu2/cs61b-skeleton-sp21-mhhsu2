# Gitlet Design Document

**Name**: Min-Hsiu Hsu

## Classes and Data Structures

### Main
This is the entry point to our program. 
It takes in arguments from the command line and 
based on the command (the first element of the args array) 
calls the corresponding command in Repository 
which will actually execute the logic of the command. 
It also validates the arguments based on the command 
to ensure that enough arguments were passed in.

#### Fields

This class has no fields and hence no associated state.


### Repository

This class represents the commit tree. 
It stores the references of commits and the head of this repository.

#### Fields

Constants:
1. `public static final File CWD = new File(System.getProperty("user.dir"));` The current working directory.
2. `public static final File GITLET_DIR = join(CWD, ".gitlet");` The .gitlet directory.
3. `public static final File COMMIT_DIR = join(GITLET_DIR, "commits");` The commits directory.
4. `public static final File OBJECT_DIR = join(GITLET_DIR, "objects");` The objects directory storing hashed files.

Instance variables:
1. `head`: the pointer points at the latest commit.
2. `branches` a data structure stores all the reference of branches in this repository
3. `commits`: a data structure stores all the references of commits in the corresponding branch.


### Commit

This class represents an individual commit.

#### Fields

1. parentId: the SHA-1 code of the parent of the current commit.
2. date: the date of the commit. 
3. commitMsg: the description of the commit.
4. blobs: a list of committed files.

## Algorithms

### Repository

1. `public static void init()` Initializes a gitlet repository, which setup the main workspace directory `.gitlet`.
2. `public void add(File f)` Adds file `f` into the stage area.

### Commit

1. `public String getHashCode()` Returns the hash code of this object.
2. `public File saveCommit()` Saves this commit as a persistent file and returns the File object of this commit.
3. `public File getParentFile()` Returns the File object of the parent of a commit.

## Persistence

The directory structure is as follows:
```
CWD                             <==== Whatever the current working directory is.
└── .gitlet                     <==== All persistant data is stored within here
    ├── head                    <==== A Repository instance stored as a file
    ├── commits                 <==== Where the commits are stored
    ├   ├── commit1             <==== A single Commit instance stored as a file
    ├   ├── ...  
    ├   └── commitN              
    ├
    └── objects                  <==== All objects (serialized files) are stored in this directory
        ├── Object1              <==== A single object instance stored to a file
        ├── Object2
        ├── ...
        └── ObjectN
```

The `Repository` class will set up all persistence. It will:
1. Create the `.gitlet` folder if it doesn't exist.
2. Create the `commits` folder if it doesn't exist.
3. Create the first commit `commit1` inside the `commits` folder.
4. Create the `objects` folder if it doesn't exist.

When `gitlet add [file]` is called, we will do:
1. Check if `[file]` is changed compared to the current `state`.
   1. If `[file]` differs, store the `serialized [file]` into the `objects` folder and add `[file]` to the `staged area` with its `file address`.
   2. If `[file]` not differs, do nothing.




