[4mGIT-FETCH[24m(1)                                      Git Manual                                     [4mGIT-FETCH[24m(1)

[1mNAME[0m
       git-fetch - Download objects and refs from another repository

[1mSYNOPSIS[0m
       [4mgit[24m [4mfetch[24m [<options>] [<repository> [<refspec>...]]
       [4mgit[24m [4mfetch[24m [<options>] <group>
       [4mgit[24m [4mfetch[24m --multiple [<options>] [(<repository> | <group>)...]
       [4mgit[24m [4mfetch[24m --all [<options>]

[1mDESCRIPTION[0m
       Fetch branches and/or tags (collectively, "refs") from one or more other repositories, along with the
       objects necessary to complete their histories. Remote-tracking branches are updated (see the
       description of <refspec> below for ways to control this behavior).

       By default, any tag that points into the histories being fetched is also fetched; the effect is to
       fetch tags that point at branches that you are interested in. This default behavior can be changed by
       using the --tags or --no-tags options or by configuring remote.<name>.tagOpt. By using a refspec that
       fetches tags explicitly, you can fetch tags that do not point into branches you are interested in as
       well.

       [4mgit[24m [4mfetch[24m can fetch from either a single named repository or URL, or from several repositories at once
       if <group> is given and there is a remotes.<group> entry in the configuration file. (See [1mgit-[0m
       [1mconfig[22m(1)).

       When no remote is specified, by default the [1morigin [22mremote will be used, unless there’s an upstream
       branch configured for the current branch.

       The names of refs that are fetched, together with the object names they point at, are written to
       .[1mgit/FETCH_HEAD[22m. This information may be used by scripts or other git commands, such as [1mgit-pull[22m(1).

[1mOPTIONS[0m
       --[no-]all
           Fetch all remotes, except for the ones that has the [1mremote.[4m[22m<name>[24m[1m.skipFetchAll [22mconfiguration
           variable set. This overrides the configuration variable fetch.all`.

       -a, --append
           Append ref names and object names of fetched refs to the existing contents of .[1mgit/FETCH_HEAD[22m.
           Without this option old data in .[1mgit/FETCH_HEAD [22mwill be overwritten.

       --atomic
           Use an atomic transaction to update local refs. Either all refs are updated, or on error, no refs
           are updated.

       --depth=<depth>
           Limit fetching to the specified number of commits from the tip of each remote branch history. If
           fetching to a [4mshallow[24m repository created by [1mgit clone [22mwith [1m--depth=[4m[22m<depth>[24m option (see [1mgit-[0m
           [1mclone[22m(1)), deepen or shorten the history to the specified number of commits. Tags for the deepened
           commits are not fetched.

       --deepen=<depth>
           Similar to --depth, except it specifies the number of commits from the current shallow boundary
           instead of from the tip of each remote branch history.

       --shallow-since=<date>
           Deepen or shorten the history of a shallow repository to include all reachable commits after
           <date>.

       --shallow-exclude=<ref>
           Deepen or shorten the history of a shallow repository to exclude commits reachable from a
           specified remote branch or tag. This option can be specified multiple times.

       --unshallow
           If the source repository is complete, convert a shallow repository to a complete one, removing all
           the limitations imposed by shallow repositories.

           If the source repository is shallow, fetch as much as possible so that the current repository has
           the same history as the source repository.

       --update-shallow
           By default when fetching from a shallow repository, [1mgit fetch [22mrefuses refs that require updating
           .git/shallow. This option updates .git/shallow and accepts such refs.

       --negotiation-tip=<commit|glob>
           By default, Git will report, to the server, commits reachable from all local refs to find common
           commits in an attempt to reduce the size of the to-be-received packfile. If specified, Git will
           only report commits reachable from the given tips. This is useful to speed up fetches when the
           user knows which local ref is likely to have commits in common with the upstream ref being
           fetched.

           This option may be specified more than once; if so, Git will report commits reachable from any of
           the given commits.

           The argument to this option may be a glob on ref names, a ref, or the (possibly abbreviated) SHA-1
           of a commit. Specifying a glob is equivalent to specifying this option multiple times, one for
           each matching ref name.

           See also the [1mfetch.negotiationAlgorithm [22mand [1mpush.negotiate [22mconfiguration variables documented in
           [1mgit-config[22m(1), and the [1m--negotiate-only [22moption below.

       --negotiate-only
           Do not fetch anything from the server, and instead print the ancestors of the provided
           [1m--negotiation-tip=* [22marguments, which we have in common with the server.

           This is incompatible with [1m--recurse-submodules=[22m[[1myes[22m|[1mon-demand[22m]. Internally this is used to
           implement the [1mpush.negotiate [22moption, see [1mgit-config[22m(1).

       --dry-run
           Show what would be done, without making any changes.

       --porcelain
           Print the output to standard output in an easy-to-parse format for scripts. See section OUTPUT in
           [1mgit-fetch[22m(1) for details.

           This is incompatible with [1m--recurse-submodules=[22m[[1myes[22m|[1mon-demand[22m] and takes precedence over the
           [1mfetch.output [22mconfig option.

       --[no-]write-fetch-head
           Write the list of remote refs fetched in the [1mFETCH_HEAD [22mfile directly under [1m$GIT_DIR[22m. This is the
           default. Passing [1m--no-write-fetch-head [22mfrom the command line tells Git not to write the file.
           Under [1m--dry-run [22moption, the file is never written.

       -f, --force
           When [4mgit[24m [4mfetch[24m is used with [4m<src>[24m[1m:[4m[22m<dst>[24m refspec, it may refuse to update the local branch as
           discussed in the [4m<refspec>[24m part below. This option overrides that check.

       -k, --keep
           Keep downloaded pack.

       --multiple
           Allow several <repository> and <group> arguments to be specified. No <refspec>s may be specified.

       --[no-]auto-maintenance, --[no-]auto-gc
           Run [1mgit maintenance run --auto [22mat the end to perform automatic repository maintenance if needed.
           ([1m--[22m[[1mno-[22m][1mauto-gc [22mis a synonym.) This is enabled by default.

       --[no-]write-commit-graph
           Write a commit-graph after fetching. This overrides the config setting [1mfetch.writeCommitGraph[22m.

       --prefetch
           Modify the configured refspec to place all refs into the [1mrefs/prefetch/ [22mnamespace. See the
           [1mprefetch [22mtask in [1mgit-maintenance[22m(1).

       -p, --prune
           Before fetching, remove any remote-tracking references that no longer exist on the remote. Tags
           are not subject to pruning if they are fetched only because of the default tag auto-following or
           due to a --tags option. However, if tags are fetched due to an explicit refspec (either on the
           command line or in the remote configuration, for example if the remote was cloned with the
           --mirror option), then they are also subject to pruning. Supplying [1m--prune-tags [22mis a shorthand for
           providing the tag refspec.

           See the PRUNING section below for more details.

       -P, --prune-tags
           Before fetching, remove any local tags that no longer exist on the remote if [1m--prune [22mis enabled.
           This option should be used more carefully, unlike [1m--prune [22mit will remove any local references
           (local tags) that have been created. This option is a shorthand for providing the explicit tag
           refspec along with [1m--prune[22m, see the discussion about that in its documentation.

           See the PRUNING section below for more details.

       -n, --no-tags
           By default, tags that point at objects that are downloaded from the remote repository are fetched
           and stored locally. This option disables this automatic tag following. The default behavior for a
           remote may be specified with the remote.<name>.tagOpt setting. See [1mgit-config[22m(1).

       --refetch
           Instead of negotiating with the server to avoid transferring commits and associated objects that
           are already present locally, this option fetches all objects as a fresh clone would. Use this to
           reapply a partial clone filter from configuration or using [1m--filter= [22mwhen the filter definition
           has changed. Automatic post-fetch maintenance will perform object database pack consolidation to
           remove any duplicate objects.

       --refmap=<refspec>
           When fetching refs listed on the command line, use the specified refspec (can be given more than
           once) to map the refs to remote-tracking branches, instead of the values of [1mremote.*.fetch[0m
           configuration variables for the remote repository. Providing an empty [4m<refspec>[24m to the [1m--refmap[0m
           option causes Git to ignore the configured refspecs and rely entirely on the refspecs supplied as
           command-line arguments. See section on "Configured Remote-tracking Branches" for details.

       -t, --tags
           Fetch all tags from the remote (i.e., fetch remote tags [1mrefs/tags/* [22minto local tags with the same
           name), in addition to whatever else would otherwise be fetched. Using this option alone does not
           subject tags to pruning, even if --prune is used (though tags may be pruned anyway if they are
           also the destination of an explicit refspec; see [1m--prune[22m).

       --recurse-submodules[=(yes|on-demand|no)]
           This option controls if and under what conditions new commits of submodules should be fetched too.
           When recursing through submodules, [1mgit fetch [22malways attempts to fetch "changed" submodules, that
           is, a submodule that has commits that are referenced by a newly fetched superproject commit but
           are missing in the local submodule clone. A changed submodule can be fetched as long as it is
           present locally e.g. in [1m$GIT_DIR/modules/ [22m(see [1mgitsubmodules[22m(7)); if the upstream adds a new
           submodule, that submodule cannot be fetched until it is cloned e.g. by [1mgit submodule update[22m.

           When set to [4mon-demand[24m, only changed submodules are fetched. When set to [4myes[24m, all populated
           submodules are fetched and submodules that are both unpopulated and changed are fetched. When set
           to [4mno[24m, submodules are never fetched.

           When unspecified, this uses the value of [1mfetch.recurseSubmodules [22mif it is set (see [1mgit-config[22m(1)),
           defaulting to [4mon-demand[24m if unset. When this option is used without any value, it defaults to [4myes[24m.

       -j, --jobs=<n>
           Number of parallel children to be used for all forms of fetching.

           If the [1m--multiple [22moption was specified, the different remotes will be fetched in parallel. If
           multiple submodules are fetched, they will be fetched in parallel. To control them independently,
           use the config settings [1mfetch.parallel [22mand [1msubmodule.fetchJobs [22m(see [1mgit-config[22m(1)).

           Typically, parallel recursive and multi-remote fetches will be faster. By default fetches are
           performed sequentially, not in parallel.

       --no-recurse-submodules
           Disable recursive fetching of submodules (this has the same effect as using the
           [1m--recurse-submodules=no [22moption).

       --set-upstream
           If the remote is fetched successfully, add upstream (tracking) reference, used by argument-less
           [1mgit-pull[22m(1) and other commands. For more information, see [1mbranch.[4m[22m<name>[24m[1m.merge [22mand
           [1mbranch.[4m[22m<name>[24m[1m.remote [22min [1mgit-config[22m(1).

       --submodule-prefix=<path>
           Prepend <path> to paths printed in informative messages such as "Fetching submodule foo". This
           option is used internally when recursing over submodules.

       --recurse-submodules-default=[yes|on-demand]
           This option is used internally to temporarily provide a non-negative default value for the
           --recurse-submodules option. All other methods of configuring fetch’s submodule recursion (such as
           settings in [1mgitmodules[22m(5) and [1mgit-config[22m(1)) override this option, as does specifying
           --[no-]recurse-submodules directly.

       -u, --update-head-ok
           By default [4mgit[24m [4mfetch[24m refuses to update the head which corresponds to the current branch. This flag
           disables the check. This is purely for the internal use for [4mgit[24m [4mpull[24m to communicate with [4mgit[0m
           [4mfetch[24m, and unless you are implementing your own Porcelain you are not supposed to use it.

       --upload-pack <upload-pack>
           When given, and the repository to fetch from is handled by [4mgit[24m [4mfetch-pack[24m, [1m--exec=[4m[22m<upload-pack>[24m is
           passed to the command to specify non-default path for the command run on the other end.

       -q, --quiet
           Pass --quiet to git-fetch-pack and silence any other internally used git commands. Progress is not
           reported to the standard error stream.

       -v, --verbose
           Be verbose.

       --progress
           Progress status is reported on the standard error stream by default when it is attached to a
           terminal, unless -q is specified. This flag forces progress status even if the standard error
           stream is not directed to a terminal.

       -o <option>, --server-option=<option>
           Transmit the given string to the server when communicating using protocol version 2. The given
           string must not contain a NUL or LF character. The server’s handling of server options, including
           unknown ones, is server-specific. When multiple [1m--server-option=[4m[22m<option>[24m are given, they are all
           sent to the other side in the order listed on the command line. When no [1m--server-option=[4m[22m<option>[0m
           is given from the command line, the values of configuration variable [1mremote.[4m[22m<name>[24m[1m.serverOption[0m
           are used instead.

       --show-forced-updates
           By default, git checks if a branch is force-updated during fetch. This can be disabled through
           fetch.showForcedUpdates, but the --show-forced-updates option guarantees this check occurs. See
           [1mgit-config[22m(1).

       --no-show-forced-updates
           By default, git checks if a branch is force-updated during fetch. Pass --no-show-forced-updates or
           set fetch.showForcedUpdates to false to skip this check for performance reasons. If used during
           [4mgit-pull[24m the --ff-only option will still check for forced updates before attempting a fast-forward
           update. See [1mgit-config[22m(1).

       -4, --ipv4
           Use IPv4 addresses only, ignoring IPv6 addresses.

       -6, --ipv6
           Use IPv6 addresses only, ignoring IPv4 addresses.

       <repository>
           The "remote" repository that is the source of a fetch or pull operation. This parameter can be
           either a URL (see the section GIT URLS below) or the name of a remote (see the section REMOTES
           below).

       <group>
           A name referring to a list of repositories as the value of remotes.<group> in the configuration
           file. (See [1mgit-config[22m(1)).

       <refspec>
           Specifies which refs to fetch and which local refs to update. When no <refspec>s appear on the
           command line, the refs to fetch are read from [1mremote.[4m[22m<repository>[24m[1m.fetch [22mvariables instead (see
           CONFIGURED REMOTE-TRACKING BRANCHES below).

           The format of a <refspec> parameter is an optional plus [1m+[22m, followed by the source <src>, followed
           by a colon [1m:[22m, followed by the destination <dst>. The colon can be omitted when <dst> is empty.
           <src> is typically a ref, or a glob pattern with a single [1m* [22mthat is used to match a set of refs,
           but it can also be a fully spelled hex object name.

           A <refspec> may contain a [1m* [22min its <src> to indicate a simple pattern match. Such a refspec
           functions like a glob that matches any ref with the pattern. A pattern <refspec> must have one and
           only one [1m* [22min both the <src> and <dst>. It will map refs to the destination by replacing the [1m*[0m
           with the contents matched from the source.

           If a refspec is prefixed by [1m^[22m, it will be interpreted as a negative refspec. Rather than
           specifying which refs to fetch or which local refs to update, such a refspec will instead specify
           refs to exclude. A ref will be considered to match if it matches at least one positive refspec,
           and does not match any negative refspec. Negative refspecs can be useful to restrict the scope of
           a pattern refspec so that it will not include specific refs. Negative refspecs can themselves be
           pattern refspecs. However, they may only contain a <src> and do not specify a <dst>. Fully spelled
           out hex object names are also not supported.

           [1mtag [4m[22m<tag>[24m means the same as [1mrefs/tags/[4m[22m<tag>[24m[1m:refs/tags/[4m[22m<tag>[24m; it requests fetching everything up to
           the given tag.

           The remote ref that matches <src> is fetched, and if <dst> is not an empty string, an attempt is
           made to update the local ref that matches it.

           Whether that update is allowed without [1m--force [22mdepends on the ref namespace it’s being fetched to,
           the type of object being fetched, and whether the update is considered to be a fast-forward.
           Generally, the same rules apply for fetching as when pushing, see the [4m<refspec>[24m... section of [1mgit-[0m
           [1mpush[22m(1) for what those are. Exceptions to those rules particular to [4mgit[24m [4mfetch[24m are noted below.

           Until Git version 2.20, and unlike when pushing with [1mgit-push[22m(1), any updates to [1mrefs/tags/* [22mwould
           be accepted without [1m+ [22min the refspec (or [1m--force[22m). When fetching, we promiscuously considered all
           tag updates from a remote to be forced fetches. Since Git version 2.20, fetching to update
           [1mrefs/tags/* [22mworks the same way as when pushing. I.e. any updates will be rejected without [1m+ [22min the
           refspec (or [1m--force[22m).

           Unlike when pushing with [1mgit-push[22m(1), any updates outside of [1mrefs/[22m{tags,heads}/* will be accepted
           without [1m+ [22min the refspec (or [1m--force[22m), whether that’s swapping e.g. a tree object for a blob, or a
           commit for another commit that doesn’t have the previous commit as an ancestor etc.

           Unlike when pushing with [1mgit-push[22m(1), there is no configuration which’ll amend these rules, and
           nothing like a [1mpre-fetch [22mhook analogous to the [1mpre-receive [22mhook.

           As with pushing with [1mgit-push[22m(1), all of the rules described above about what’s not allowed as an
           update can be overridden by adding an optional leading [1m+ [22mto a refspec (or using the [1m--force[0m
           command line option). The only exception to this is that no amount of forcing will make the
           [1mrefs/heads/* [22mnamespace accept a non-commit object.

               [1mNote[0m
               When the remote branch you want to fetch is known to be rewound and rebased regularly, it is
               expected that its new tip will not be a descendant of its previous tip (as stored in your
               remote-tracking branch the last time you fetched). You would want to use the [1m+ [22msign to
               indicate non-fast-forward updates will be needed for such branches. There is no way to
               determine or declare that a branch will be made available in a repository with this behavior;
               the pulling user simply must know this is the expected usage pattern for a branch.

       --stdin
           Read refspecs, one per line, from stdin in addition to those provided as arguments. The "tag
           <name>" format is not supported.

[1mGIT URLS[0m
       In general, URLs contain information about the transport protocol, the address of the remote server,
       and the path to the repository. Depending on the transport protocol, some of this information may be
       absent.

       Git supports ssh, git, http, and https protocols (in addition, ftp and ftps can be used for fetching,
       but this is inefficient and deprecated; do not use them).

       The native transport (i.e. [1mgit:// [22mURL) does no authentication and should be used with caution on
       unsecured networks.

       The following syntaxes may be used with them:

       •   [1mssh://[22m[[4m<user>[24m[1m@[22m][4m<host>[24m[[1m:[4m[22m<port>[24m][1m/[4m[22m<path-to-git-repo>[0m

       •   [1mgit://[4m[22m<host>[24m[[1m:[4m[22m<port>[24m][1m/[4m[22m<path-to-git-repo>[0m

       •   [1mhttp[22m[[1ms[22m][1m://[4m[22m<host>[24m[[1m:[4m[22m<port>[24m][1m/[4m[22m<path-to-git-repo>[0m

       •   [1mftp[22m[[1ms[22m][1m://[4m[22m<host>[24m[[1m:[4m[22m<port>[24m][1m/[4m[22m<path-to-git-repo>[0m

       An alternative scp-like syntax may also be used with the ssh protocol:

       •   [[4m<user>[24m[1m@[22m][4m<host>[24m[1m:/[4m[22m<path-to-git-repo>[0m

       This syntax is only recognized if there are no slashes before the first colon. This helps
       differentiate a local path that contains a colon. For example the local path [1mfoo:bar [22mcould be
       specified as an absolute path or .[1m/foo:bar [22mto avoid being misinterpreted as an ssh url.

       The ssh and git protocols additionally support [1m~[4m[22m<username>[24m expansion:

       •   [1mssh://[22m[[4m<user>[24m[1m@[22m][4m<host>[24m[[1m:[4m[22m<port>[24m][1m/~[4m[22m<user>[24m[1m/[4m[22m<path-to-git-repo>[0m

       •   [1mgit://[4m[22m<host>[24m[[1m:[4m[22m<port>[24m][1m/~[4m[22m<user>[24m[1m/[4m[22m<path-to-git-repo>[0m

       •   [[4m<user>[24m[1m@[22m][4m<host>[24m[1m:~[4m[22m<user>[24m[1m/[4m[22m<path-to-git-repo>[0m

       For local repositories, also supported by Git natively, the following syntaxes may be used:

       •   [1m/path/to/repo.git/[0m

       •   [1mfile:///path/to/repo.git/[0m

       These two syntaxes are mostly equivalent, except when cloning, when the former implies [1m--local [22moption.
       See [1mgit-clone[22m(1) for details.

       [1mgit clone[22m, [1mgit fetch [22mand [1mgit pull[22m, but not [1mgit push[22m, will also accept a suitable bundle file. See [1mgit-[0m
       [1mbundle[22m(1).

       When Git doesn’t know how to handle a certain transport protocol, it attempts to use the
       [1mremote-[4m[22m<transport>[24m remote helper, if one exists. To explicitly request a remote helper, the following
       syntax may be used:

       •   [4m<transport>[24m[1m::[4m[22m<address>[0m

       where [4m<address>[24m may be a path, a server and path, or an arbitrary URL-like string recognized by the
       specific remote helper being invoked. See [1mgitremote-helpers[22m(7) for details.

       If there are a large number of similarly-named remote repositories and you want to use a different
       format for them (such that the URLs you use will be rewritten into URLs that work), you can create a
       configuration section of the form:

                   [url "[4m<actual-url-base>[24m"]
                           insteadOf = [4m<other-url-base>[0m

       For example, with this:

                   [url "git://git.host.xz/"]
                           insteadOf = host.xz:/path/to/
                           insteadOf = work:

       a URL like "work:repo.git" or like "host.xz:/path/to/repo.git" will be rewritten in any context that
       takes a URL to be "git://git.host.xz/repo.git".

       If you want to rewrite URLs for push only, you can create a configuration section of the form:

                   [url "[4m<actual-url-base>[24m"]
                           pushInsteadOf = [4m<other-url-base>[0m

       For example, with this:

                   [url "ssh://example.org/"]
                           pushInsteadOf = git://example.org/

       a URL like "git://example.org/path/to/repo.git" will be rewritten to
       "ssh://example.org/path/to/repo.git" for pushes, but pulls will still use the original URL.

[1mREMOTES[0m
       The name of one of the following can be used instead of a URL as [4m<repository>[24m argument:

       •   a remote in the Git configuration file: [1m$GIT_DIR/config[22m,

       •   a file in the [1m$GIT_DIR/remotes [22mdirectory, or

       •   a file in the [1m$GIT_DIR/branches [22mdirectory.

       All of these also allow you to omit the refspec from the command line because they each contain a
       refspec which git will use by default.

   [1mNamed remote in configuration file[0m
       You can choose to provide the name of a remote which you had previously configured using [1mgit-[0m
       [1mremote[22m(1), [1mgit-config[22m(1) or even by a manual edit to the [1m$GIT_DIR/config [22mfile. The URL of this remote
       will be used to access the repository. The refspec of this remote will be used by default when you do
       not provide a refspec on the command line. The entry in the config file would appear like this:

                   [remote "<name>"]
                           url = <URL>
                           pushurl = <pushurl>
                           push = <refspec>
                           fetch = <refspec>

       The [4m<pushurl>[24m is used for pushes only. It is optional and defaults to [4m<URL>[24m. Pushing to a remote
       affects all defined pushurls or all defined urls if no pushurls are defined. Fetch, however, will only
       fetch from the first defined url if multiple urls are defined.

   [1mNamed file in $GIT_DIR/remotes[0m
       You can choose to provide the name of a file in [1m$GIT_DIR/remotes[22m. The URL in this file will be used to
       access the repository. The refspec in this file will be used as default when you do not provide a
       refspec on the command line. This file should have the following format:

                   URL: one of the above URL formats
                   Push: <refspec>
                   Pull: <refspec>

       [1mPush: [22mlines are used by [4mgit[24m [4mpush[24m and [1mPull: [22mlines are used by [4mgit[24m [4mpull[24m and [4mgit[24m [4mfetch[24m. Multiple [1mPush:[0m
       and [1mPull: [22mlines may be specified for additional branch mappings.

   [1mNamed file in $GIT_DIR/branches[0m
       You can choose to provide the name of a file in [1m$GIT_DIR/branches[22m. The URL in this file will be used
       to access the repository. This file should have the following format:

                   <URL>#<head>

       [4m<URL>[24m is required; #[4m<head>[24m is optional.

       Depending on the operation, git will use one of the following refspecs, if you don’t provide one on
       the command line. [4m<branch>[24m is the name of this file in [1m$GIT_DIR/branches [22mand [4m<head>[24m defaults to
       [1mmaster[22m.

       git fetch uses:

                   refs/heads/<head>:refs/heads/<branch>

       git push uses:

                   HEAD:refs/heads/<head>

[1mCONFIGURED REMOTE-TRACKING BRANCHES[0m
       You often interact with the same remote repository by regularly and repeatedly fetching from it. In
       order to keep track of the progress of such a remote repository, [1mgit fetch [22mallows you to configure
       [1mremote.[4m[22m<repository>[24m[1m.fetch [22mconfiguration variables.

       Typically such a variable may look like this:

           [remote "origin"]
                   fetch = +refs/heads/*:refs/remotes/origin/*

       This configuration is used in two ways:

       •   When [1mgit fetch [22mis run without specifying what branches and/or tags to fetch on the command line,
           e.g.  [1mgit fetch origin [22mor [1mgit fetch[22m, [1mremote.[4m[22m<repository>[24m[1m.fetch [22mvalues are used as the refspecs—
           they specify which refs to fetch and which local refs to update. The example above will fetch all
           branches that exist in the [1morigin [22m(i.e. any ref that matches the left-hand side of the value,
           [1mrefs/heads/*[22m) and update the corresponding remote-tracking branches in the [1mrefs/remotes/origin/*[0m
           hierarchy.

       •   When [1mgit fetch [22mis run with explicit branches and/or tags to fetch on the command line, e.g.  [1mgit[0m
           [1mfetch origin master[22m, the <refspec>s given on the command line determine what are to be fetched
           (e.g.  [1mmaster [22min the example, which is a short-hand for [1mmaster:[22m, which in turn means "fetch the
           [4mmaster[24m branch but I do not explicitly say what remote-tracking branch to update with it from the
           command line"), and the example command will fetch [4monly[24m the [4mmaster[24m branch. The
           [1mremote.[4m[22m<repository>[24m[1m.fetch [22mvalues determine which remote-tracking branch, if any, is updated. When
           used in this way, the [1mremote.[4m[22m<repository>[24m[1m.fetch [22mvalues do not have any effect in deciding [4mwhat[0m
           gets fetched (i.e. the values are not used as refspecs when the command-line lists refspecs); they
           are only used to decide [4mwhere[24m the refs that are fetched are stored by acting as a mapping.

       The latter use of the [1mremote.[4m[22m<repository>[24m[1m.fetch [22mvalues can be overridden by giving the
       [1m--refmap=[4m[22m<refspec>[24m parameter(s) on the command line.

[1mPRUNING[0m
       Git has a default disposition of keeping data unless it’s explicitly thrown away; this extends to
       holding onto local references to branches on remotes that have themselves deleted those branches.

       If left to accumulate, these stale references might make performance worse on big and busy repos that
       have a lot of branch churn, and e.g. make the output of commands like [1mgit branch -a --contains[0m
       [4m<commit>[24m needlessly verbose, as well as impacting anything else that’ll work with the complete set of
       known references.

       These remote-tracking references can be deleted as a one-off with either of:

           # While fetching
           $ git fetch --prune <name>

           # Only prune, don't fetch
           $ git remote prune <name>

       To prune references as part of your normal workflow without needing to remember to run that, set
       [1mfetch.prune [22mglobally, or [1mremote.[4m[22m<name>[24m[1m.prune [22mper-remote in the config. See [1mgit-config[22m(1).

       Here’s where things get tricky and more specific. The pruning feature doesn’t actually care about
       branches, instead it’ll prune local ←→ remote-references as a function of the refspec of the remote
       (see [4m<refspec>[24m and CONFIGURED REMOTE-TRACKING BRANCHES above).

       Therefore if the refspec for the remote includes e.g. [1mrefs/tags/*:refs/tags/*[22m, or you manually run
       e.g. [1mgit fetch --prune [4m[22m<name>[24m "refs/tags/*:refs/tags/*" it won’t be stale remote tracking branches
       that are deleted, but any local tag that doesn’t exist on the remote.

       This might not be what you expect, i.e. you want to prune remote [4m<name>[24m, but also explicitly fetch
       tags from it, so when you fetch from it you delete all your local tags, most of which may not have
       come from the [4m<name>[24m remote in the first place.

       So be careful when using this with a refspec like [1mrefs/tags/*:refs/tags/*[22m, or any other refspec which
       might map references from multiple remotes to the same local namespace.

       Since keeping up-to-date with both branches and tags on the remote is a common use-case the
       [1m--prune-tags [22moption can be supplied along with [1m--prune [22mto prune local tags that don’t exist on the
       remote, and force-update those tags that differ. Tag pruning can also be enabled with [1mfetch.pruneTags[0m
       or [1mremote.[4m[22m<name>[24m[1m.pruneTags [22min the config. See [1mgit-config[22m(1).

       The [1m--prune-tags [22moption is equivalent to having [1mrefs/tags/*:refs/tags/* [22mdeclared in the refspecs of
       the remote. This can lead to some seemingly strange interactions:

           # These both fetch tags
           $ git fetch --no-tags origin 'refs/tags/*:refs/tags/*'
           $ git fetch --no-tags --prune-tags origin

       The reason it doesn’t error out when provided without [1m--prune [22mor its config versions is for
       flexibility of the configured versions, and to maintain a 1=1 mapping between what the command line
       flags do, and what the configuration versions do.

       It’s reasonable to e.g. configure [1mfetch.pruneTags=true [22min [1m~/.gitconfig [22mto have tags pruned whenever
       [1mgit fetch --prune [22mis run, without making every invocation of [1mgit fetch [22mwithout [1m--prune [22man error.

       Pruning tags with [1m--prune-tags [22malso works when fetching a URL instead of a named remote. These will
       all prune tags not found on origin:

           $ git fetch origin --prune --prune-tags
           $ git fetch origin --prune 'refs/tags/*:refs/tags/*'
           $ git fetch <url-of-origin> --prune --prune-tags
           $ git fetch <url-of-origin> --prune 'refs/tags/*:refs/tags/*'

[1mOUTPUT[0m
       The output of "git fetch" depends on the transport method used; this section describes the output when
       fetching over the Git protocol (either locally or via ssh) and Smart HTTP protocol.

       The status of the fetch is output in tabular form, with each line representing the status of a single
       ref. Each line is of the form:

            <flag> <summary> <from> -> <to> [<reason>]

       When using [1m--porcelain[22m, the output format is intended to be machine-parseable. In contrast to the
       human-readable output formats it thus prints to standard output instead of standard error. Each line
       is of the form:

           <flag> <old-object-id> <new-object-id> <local-reference>

       The status of up-to-date refs is shown only if the --verbose option is used.

       In compact output mode, specified with configuration variable fetch.output, if either entire [4m<from>[24m or
       [4m<to>[24m is found in the other string, it will be substituted with [1m* [22min the other string. For example,
       [1mmaster -[22m> [1morigin/master [22mbecomes [1mmaster -[22m> [1morigin/*[22m.

       flag
           A single character indicating the status of the ref:

           (space)
               for a successfully fetched fast-forward;

           [1m+[0m
               for a successful forced update;

           [1m-[0m
               for a successfully pruned ref;

           [1mt[0m
               for a successful tag update;

           [1m*[0m
               for a successfully fetched new ref;

           !
               for a ref that was rejected or failed to update; and

           [1m=[0m
               for a ref that was up to date and did not need fetching.

       summary
           For a successfully fetched ref, the summary shows the old and new values of the ref in a form
           suitable for using as an argument to [1mgit log [22m(this is [4m<old>[24m[1m..[4m[22m<new>[24m in most cases, and
           [4m<old>[24m[1m...[4m[22m<new>[24m for forced non-fast-forward updates).

       from
           The name of the remote ref being fetched from, minus its [1mrefs/[4m[22m<type>[24m[1m/ [22mprefix. In the case of
           deletion, the name of the remote ref is "(none)".

       to
           The name of the local ref being updated, minus its [1mrefs/[4m[22m<type>[24m[1m/ [22mprefix.

       reason
           A human-readable explanation. In the case of successfully fetched refs, no explanation is needed.
           For a failed ref, the reason for failure is described.

[1mEXAMPLES[0m
       •   Update the remote-tracking branches:

               $ git fetch origin

           The above command copies all branches from the remote [1mrefs/heads/ [22mnamespace and stores them to the
           local [1mrefs/remotes/origin/ [22mnamespace, unless the [1mremote.[4m[22m<repository>[24m[1m.fetch [22moption is used to
           specify a non-default refspec.

       •   Using refspecs explicitly:

               $ git fetch origin +seen:seen maint:tmp

           This updates (or creates, as necessary) branches [1mseen [22mand [1mtmp [22min the local repository by fetching
           from the branches (respectively) [1mseen [22mand [1mmaint [22mfrom the remote repository.

           The [1mseen [22mbranch will be updated even if it does not fast-forward, because it is prefixed with a
           plus sign; [1mtmp [22mwill not be.

       •   Peek at a remote’s branch, without configuring the remote in your local repository:

               $ git fetch git://git.kernel.org/pub/scm/git/git.git maint
               $ git log FETCH_HEAD

           The first command fetches the [1mmaint [22mbranch from the repository at
           [1mgit://git.kernel.org/pub/scm/git/git.git [22mand the second command uses [1mFETCH_HEAD [22mto examine the
           branch with [1mgit-log[22m(1). The fetched objects will eventually be removed by git’s built-in
           housekeeping (see [1mgit-gc[22m(1)).

[1mSECURITY[0m
       The fetch and push protocols are not designed to prevent one side from stealing data from the other
       repository that was not intended to be shared. If you have private data that you need to protect from
       a malicious peer, your best option is to store it in another repository. This applies to both clients
       and servers. In particular, namespaces on a server are not effective for read access control; you
       should only grant read access to a namespace to clients that you would trust with read access to the
       entire repository.

       The known attack vectors are as follows:

        1. The victim sends "have" lines advertising the IDs of objects it has that are not explicitly
           intended to be shared but can be used to optimize the transfer if the peer also has them. The
           attacker chooses an object ID X to steal and sends a ref to X, but isn’t required to send the
           content of X because the victim already has it. Now the victim believes that the attacker has X,
           and it sends the content of X back to the attacker later. (This attack is most straightforward for
           a client to perform on a server, by creating a ref to X in the namespace the client has access to
           and then fetching it. The most likely way for a server to perform it on a client is to "merge" X
           into a public branch and hope that the user does additional work on this branch and pushes it back
           to the server without noticing the merge.)

        2. As in #1, the attacker chooses an object ID X to steal. The victim sends an object Y that the
           attacker already has, and the attacker falsely claims to have X and not Y, so the victim sends Y
           as a delta against X. The delta reveals regions of X that are similar to Y to the attacker.

[1mCONFIGURATION[0m
       Everything below this line in this section is selectively included from the [1mgit-config[22m(1)
       documentation. The content is the same as what’s found there:

       fetch.recurseSubmodules
           This option controls whether [1mgit fetch [22m(and the underlying fetch in [1mgit pull[22m) will recursively
           fetch into populated submodules. This option can be set either to a boolean value or to [4mon-demand[24m.
           Setting it to a boolean changes the behavior of fetch and pull to recurse unconditionally into
           submodules when set to true or to not recurse at all when set to false. When set to [4mon-demand[24m,
           fetch and pull will only recurse into a populated submodule when its superproject retrieves a
           commit that updates the submodule’s reference. Defaults to [4mon-demand[24m, or to the value of
           [4msubmodule.recurse[24m if set.

       fetch.fsckObjects
           If it is set to true, git-fetch-pack will check all fetched objects. See [1mtransfer.fsckObjects [22mfor
           what’s checked. Defaults to false. If not set, the value of [1mtransfer.fsckObjects [22mis used instead.

       fetch.fsck.<msg-id>
           Acts like [1mfsck.[4m[22m<msg-id>[24m, but is used by [1mgit-fetch-pack[22m(1) instead of [1mgit-fsck[22m(1). See the
           [1mfsck.[4m[22m<msg-id>[24m documentation for details.

       fetch.fsck.skipList
           Acts like [1mfsck.skipList[22m, but is used by [1mgit-fetch-pack[22m(1) instead of [1mgit-fsck[22m(1). See the
           [1mfsck.skipList [22mdocumentation for details.

       fetch.unpackLimit
           If the number of objects fetched over the Git native transfer is below this limit, then the
           objects will be unpacked into loose object files. However if the number of received objects equals
           or exceeds this limit then the received pack will be stored as a pack, after adding any missing
           delta bases. Storing the pack from a push can make the push operation complete faster, especially
           on slow filesystems. If not set, the value of [1mtransfer.unpackLimit [22mis used instead.

       fetch.prune
           If true, fetch will automatically behave as if the [1m--prune [22moption was given on the command line.
           See also [1mremote.[4m[22m<name>[24m[1m.prune [22mand the PRUNING section of [1mgit-fetch[22m(1).

       fetch.pruneTags
           If true, fetch will automatically behave as if the [1mrefs/tags/*:refs/tags/* [22mrefspec was provided
           when pruning, if not set already. This allows for setting both this option and [1mfetch.prune [22mto
           maintain a 1=1 mapping to upstream refs. See also [1mremote.[4m[22m<name>[24m[1m.pruneTags [22mand the PRUNING section
           of [1mgit-fetch[22m(1).

       fetch.all
           If true, fetch will attempt to update all available remotes. This behavior can be overridden by
           passing [1m--no-all [22mor by explicitly specifying one or more remote(s) to fetch from. Defaults to
           false.

       fetch.output
           Control how ref update status is printed. Valid values are [1mfull [22mand [1mcompact[22m. Default value is
           [1mfull[22m. See the OUTPUT section in [1mgit-fetch[22m(1) for details.

       fetch.negotiationAlgorithm
           Control how information about the commits in the local repository is sent when negotiating the
           contents of the packfile to be sent by the server. Set to "consecutive" to use an algorithm that
           walks over consecutive commits checking each one. Set to "skipping" to use an algorithm that skips
           commits in an effort to converge faster, but may result in a larger-than-necessary packfile; or
           set to "noop" to not send any information at all, which will almost certainly result in a
           larger-than-necessary packfile, but will skip the negotiation step. Set to "default" to override
           settings made previously and use the default behaviour. The default is normally "consecutive", but
           if [1mfeature.experimental [22mis true, then the default is "skipping". Unknown values will cause [4mgit[0m
           [4mfetch[24m to error out.

           See also the [1m--negotiate-only [22mand [1m--negotiation-tip [22moptions to [1mgit-fetch[22m(1).

       fetch.showForcedUpdates
           Set to false to enable [1m--no-show-forced-updates [22min [1mgit-fetch[22m(1) and [1mgit-pull[22m(1) commands. Defaults
           to true.

       fetch.parallel
           Specifies the maximal number of fetch operations to be run in parallel at a time (submodules, or
           remotes when the [1m--multiple [22moption of [1mgit-fetch[22m(1) is in effect).

           A value of 0 will give some reasonable default. If unset, it defaults to 1.

           For submodules, this setting can be overridden using the [1msubmodule.fetchJobs [22mconfig setting.

       fetch.writeCommitGraph
           Set to true to write a commit-graph after every [1mgit fetch [22mcommand that downloads a pack-file from
           a remote. Using the [1m--split [22moption, most executions will create a very small commit-graph file on
           top of the existing commit-graph file(s). Occasionally, these files will merge and the write may
           take longer. Having an updated commit-graph file helps performance of many Git commands, including
           [1mgit merge-base[22m, [1mgit push -f[22m, and [1mgit log --graph[22m. Defaults to false.

       fetch.bundleURI
           This value stores a URI for downloading Git object data from a bundle URI before performing an
           incremental fetch from the origin Git server. This is similar to how the [1m--bundle-uri [22moption
           behaves in [1mgit-clone[22m(1).  [1mgit clone --bundle-uri [22mwill set the [1mfetch.bundleURI [22mvalue if the
           supplied bundle URI contains a bundle list that is organized for incremental fetches.

           If you modify this value and your repository has a [1mfetch.bundleCreationToken [22mvalue, then remove
           that [1mfetch.bundleCreationToken [22mvalue before fetching from the new bundle URI.

       fetch.bundleCreationToken
           When using [1mfetch.bundleURI [22mto fetch incrementally from a bundle list that uses the "creationToken"
           heuristic, this config value stores the maximum [1mcreationToken [22mvalue of the downloaded bundles.
           This value is used to prevent downloading bundles in the future if the advertised [1mcreationToken [22mis
           not strictly larger than this value.

           The creation token values are chosen by the provider serving the specific bundle URI. If you
           modify the URI at [1mfetch.bundleURI[22m, then be sure to remove the value for the
           [1mfetch.bundleCreationToken [22mvalue before fetching.

[1mBUGS[0m
       Using --recurse-submodules can only fetch new commits in submodules that are present locally e.g. in
       [1m$GIT_DIR/modules/[22m. If the upstream adds a new submodule, that submodule cannot be fetched until it is
       cloned e.g. by [1mgit submodule update[22m. This is expected to be fixed in a future Git version.

[1mSEE ALSO[0m
       [1mgit-pull[22m(1)

[1mGIT[0m
       Part of the [1mgit[22m(1) suite

Git 2.51.0                                        08/18/2025                                     [4mGIT-FETCH[24m(1)
